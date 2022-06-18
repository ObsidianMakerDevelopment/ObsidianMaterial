package com.moyskleytech.obsidian.material.parsers;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.meta.ItemMeta;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.moyskleytech.obsidian.material.ItemParser;
import com.moyskleytech.obsidian.material.ObsidianItemTemplate;
import com.moyskleytech.obsidian.material.ObsidianMaterial;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public class ObsidianItemTemplateDeserialize extends JsonDeserializer<ObsidianItemTemplate> {
    @Override
    public ObsidianItemTemplate deserialize(JsonParser input, DeserializationContext arg1)
            throws IOException, JacksonException {

        TreeNode node = input.getCodec().readTree(input);
        try {
            if (node.asToken() == JsonToken.VALUE_NULL)
                return ItemParser.deserialize("AIR");
            if (node.asToken() == JsonToken.VALUE_STRING) {
                ValueNode valNode = (ValueNode) node;
                String key = valNode.asText();
                return ItemParser.deserialize(key);
            }
            if (node.asToken() == JsonToken.START_OBJECT) {
                ObjectNode object = (ObjectNode) node;
                System.out.println("Parsing "+node);
                System.out.println("-type "+node.getClass().getSimpleName());
                System.out.println("-fields ");
                AtomicReference<ObsidianItemTemplate> returnValue = new AtomicReference<>(
                        new ObsidianItemTemplate());

                object.fields().forEachRemaining(entry->{
                    System.out.println(entry.getKey()+"="+entry.getValue());
                    try {
                        TreeNode valueNode = entry.getValue();
                        if (entry.getKey().equals("meta")) {
                            JsonParser jp = valueNode.traverse();
                            jp.nextToken();
                            SerializedObject unparsedMeta = jp.readValueAs(SerializedObject.class);
                            ItemMeta meta = (ItemMeta) unserializeObject(unparsedMeta);
                            returnValue.set(returnValue.get().meta(meta));
                        } else if (entry.getKey().equals("material")) {
                            JsonParser jp = valueNode.traverse();
                            jp.nextToken();
                            returnValue.set(
                                    returnValue.get()
                                            .material(ObsidianMaterial.valueOf(jp.getText())));
                        } else if (entry.getKey().equals("enchants")) {
                            TreeNode enchantNode = valueNode;
                            Map<Enchantment, Integer> enchants = new HashMap<>();
                            enchantNode.fieldNames().forEachRemaining((enchantString) -> {
                                try {
                                    Enchantment ech = Enchantment.getByName(enchantString);
                                    JsonParser enchantLevel = enchantNode.get(enchantString).traverse();
                                    enchantLevel.nextToken();
                                    int level = enchantLevel.getIntValue();
                                    enchants.put(ech, level);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            returnValue.set(returnValue.get().enchants(enchants));
                        } else if (entry.getKey().equals("unbreakable")) {
                            JsonParser jp = valueNode.traverse();
                            jp.nextToken();
                            returnValue.set(returnValue.get().unbreakable(jp.getBooleanValue()));
                        } else if (entry.getKey().equals("lore") && valueNode.isArray()) {
                            ArrayNode arrayNode = (ArrayNode) valueNode;
                            List<String> lore = new ArrayList<>();
                            for (JsonNode jsonNode : arrayNode) {
                                lore.add(jsonNode.asText());
                            }
                            returnValue.get().lore(lore);
                        } else if (entry.getKey().equals("name")) {
                            JsonParser jp = valueNode.traverse();
                            jp.nextToken();
                            returnValue.set(returnValue.get().name(jp.getText()));
                        }
                    } catch (IOException err) {
                        err.printStackTrace();
                    }
                });
                return returnValue.get();
            }

        } catch (Throwable t) {
            t.printStackTrace();
            return new ObsidianItemTemplate(ObsidianMaterial.valueOf("STONE"));
        }
        return ItemParser.deserialize("AIR");
    }

    private Object unserializeObject(SerializedObject obj)
    {
        try {
            if(obj.value instanceof Map<?,?>)
            {
                Map<String,Object> map = (Map<String,Object>)obj.value;
                List<String> keys = new ArrayList<>(map.keySet());
                keys.forEach(key->{
                    if(map.get(key) instanceof SerializedObject)
                        map.put(key,unserializeObject((SerializedObject)map.get(key)));
                });
            }
            Class<?> clazz = Class.forName(obj.className);
            Method method1 = clazz.getDeclaredMethod("valueOf", Map.class);
            Method method2 = clazz.getDeclaredMethod("deserialize", Map.class);
            Constructor<?> method3 = clazz.getDeclaredConstructor(Map.class);
            if(method1!=null)
            {
                return method1.invoke(null, obj.value);
            }
            if(method2!=null)
            {
                return method2.invoke(null, obj.value);
            }
            if(method3!=null)
            {
                return method3.newInstance(obj.value);
            }
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj.value;
    }
    
}