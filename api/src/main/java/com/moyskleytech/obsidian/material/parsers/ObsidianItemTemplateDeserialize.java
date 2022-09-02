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
import org.bukkit.configuration.serialization.ConfigurationSerialization;
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

/**
 * Jackson JsonDeserializer for ObsidianItemTemplate
 */
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
                AtomicReference<ObsidianItemTemplate> returnValue = new AtomicReference<>(
                        new ObsidianItemTemplate());
                object.fields().forEachRemaining(entry->{
                    try {
                        TreeNode valueNode = entry.getValue();
                        if (entry.getKey().equals("meta")) {
                            JsonParser jp = valueNode.traverse();
                            jp.nextToken();
                            jp.setCodec(input.getCodec());
                            Map unparsedMeta = jp.readValueAs(Map.class);
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
                        } else if (entry.getKey().equals("durability")) {
                            JsonParser jp = valueNode.traverse();
                            jp.nextToken();
                            returnValue.set(returnValue.get().durability(jp.getShortValue()));
                        } else if (entry.getKey().equals("lore") && valueNode.isArray()) {
                            ArrayNode arrayNode = (ArrayNode) valueNode;
                            List<String> lore = new ArrayList<>();
                            for (JsonNode jsonNode : arrayNode) {
                                lore.add(jsonNode.asText());
                            }
                            returnValue.set(returnValue.get().lore(lore));
                        }else if (entry.getKey().equals("lore")){
                            System.out.println(valueNode.toString());
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

    private Object unserializeObject(Map obj)
    {
        try {
            return ConfigurationSerialization.deserializeObject((Map<String, ?>)obj);
        } catch (SecurityException | IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;
    }
    
}