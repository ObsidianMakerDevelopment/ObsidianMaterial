package com.moyskleytech.obsidian.material;

import org.bukkit.Material;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Requires Jackson FasterXML
 */
public class ItemParser {
    /**
     * Deserialize an item template
     * Requires FasterXML
     * @param str A string representing the object, could be JSON or legacy
     * @return An object template
     */
    public static ObsidianItemTemplate deserialize(String str) {
        if (str.startsWith("{")) {
            // Handle as JSON
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(str, ObsidianItemTemplate.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        // Handle as legacy String
        return new ObsidianItemTemplate(str);
    }

    /**
     * Serialize a object template
     * Requires Jackson FasterXML
     * @param itemTemplate The item template to serialize
     * @return A string representing the object
     */
    public static String serialize(ObsidianItemTemplate itemTemplate) {
        if (itemTemplate.isPure())
            return MaterialParser.serialize(itemTemplate.getMaterial());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(itemTemplate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return itemTemplate.getMaterial().name();
    }
}
