package com.moyskleytech.obsidian.material;

import org.bukkit.Material;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ItemParser {
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
        return null;
    }

    public static String serialize(ObsidianItemTemplate itemTemplate) {
        if (itemTemplate.isPure())
            return MaterialParser.serialize(itemTemplate.getMaterial());
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            return objectMapper.writeValueAsString(itemTemplate);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
