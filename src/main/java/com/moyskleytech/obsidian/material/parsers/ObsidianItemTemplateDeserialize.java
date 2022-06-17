package com.moyskleytech.obsidian.material.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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
import com.moyskleytech.obsidian.material.ItemParser;
import com.moyskleytech.obsidian.material.ObsidianItemTemplate;
import com.moyskleytech.obsidian.material.ObsidianMaterial;

public class ObsidianItemTemplateDeserialize extends JsonDeserializer<ObsidianItemTemplate> {
    @Override
    public ObsidianItemTemplate deserialize(JsonParser input, DeserializationContext arg1)
            throws IOException, JacksonException {
        JsonToken jsonToken = input.nextToken();
        if (jsonToken == JsonToken.VALUE_STRING)
            return ItemParser.deserialize(input.getText());
        else if (jsonToken == JsonToken.START_OBJECT) {
            TreeNode node = input.getCodec().readTree(input);

            AtomicReference<ObsidianItemTemplate> returnValue = new AtomicReference<>(new ObsidianItemTemplate());
            node.fieldNames().forEachRemaining((s) -> {
                try {
                    TreeNode valueNode = node.get(s);
                    if (s.equals("meta")) {
                        ItemMeta meta = valueNode.traverse().readValueAs(ItemMeta.class);
                        returnValue.set(returnValue.get().meta(meta));
                    } else if (s.equals("material")) {
                        returnValue.set(
                                returnValue.get().material(ObsidianMaterial.valueOf(valueNode.traverse().getText())));
                    } else if (s.equals("enchants")) {
                        TreeNode enchantNode = valueNode;
                        Map<Enchantment, Integer> enchants = new HashMap<>();
                        enchantNode.fieldNames().forEachRemaining((enchantString) -> {
                            try {
                                Enchantment ech = Enchantment.getByName(enchantString);
                                int level = enchantNode.get(enchantString).traverse().getIntValue();
                                enchants.put(ech, level);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        returnValue.set(returnValue.get().enchants(enchants));
                    } else if (s.equals("unbreakable")) {
                        returnValue.set(returnValue.get().unbreakable(valueNode.traverse().getBooleanValue()));
                    } else if (s.equals("lore") && valueNode.isArray()) {
                        ArrayNode arrayNode = (ArrayNode) valueNode;
                        List<String> lore = new ArrayList<>();
                        for (JsonNode jsonNode : arrayNode) {
                            lore.add(jsonNode.asText());
                        }
                        returnValue.get().lore(lore);
                    } else if (s.equals("name")) {
                        returnValue.set(returnValue.get().name(valueNode.traverse().getText()));
                    }
                } catch (IOException err) {
                    err.printStackTrace();
                }
            });
        }
        return null;
    }
}