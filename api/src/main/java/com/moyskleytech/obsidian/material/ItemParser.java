package com.moyskleytech.obsidian.material;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.moyskleytech.obsidian.material.ItemParser.Part.PartType;

import lombok.Getter;
import lombok.Setter;

/**
 * Requires Jackson FasterXML
 */
public class ItemParser {
    /**
     * Deserialize an item template
     * Requires FasterXML
     * 
     * @param str A string representing the object, could be JSON or legacy
     * @return An object template
     */
    public static ObsidianItemTemplate deserialize(String str) {
        if (str.startsWith("{")) {
            // Handle as JSON
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                return objectMapper.readValue(str, ObsidianItemTemplate.class);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        // Handle as legacy String
        return new ObsidianItemTemplate(str);
    }

    private static int index(String source, String search, int begin) {
        return Math.max(
                source.toUpperCase().indexOf(search.toUpperCase(), begin),
                source.toLowerCase().indexOf(search.toLowerCase().replaceAll("_", " "), begin));
    }

    private static int index(String source, String search) {
        return index(source, search, 0);
    }

    /**
     * Convert string to item parts
     * 
     * @param s String to be parsed
     * @return Item parts
     */
    public static List<Part> toParts(String s) {
        List<Part> ret = new ArrayList<>();

        Part materialPart = new Part();
        materialPart.type = Part.PartType.MATERIAL;
        ret.add(materialPart);

        int indexOf = index(s, "_OF_");
        int indexName = index(s, "_NAMED_");

        int nextPartIndex = -1;
        Part.PartType nextType = null;
        int lenOfType = 0, nextLenOfType = 0;
        if (indexOf >= 0) {
            nextPartIndex = indexOf;
            nextType = PartType.OF;
            lenOfType = 4;
        }
        if (indexName >= 0 && indexName < nextPartIndex || nextPartIndex == -1) {
            nextPartIndex = indexName;
            nextType = PartType.NAME;
            lenOfType = 7;
        }

        if (nextPartIndex == -1)
            materialPart.setValue(s);
        else
            materialPart.setValue(s.substring(0, nextPartIndex));

        int startsIndex = nextPartIndex;
        while (nextPartIndex != -1) {
            indexOf = index(s, "_OF_", startsIndex + 1);
            indexName = index(s, "_NAMED_", startsIndex + 1);

            Part workPart = new Part();
            workPart.type = nextType;
            ret.add(workPart);

            nextPartIndex = -1;
            if (indexOf >= 0) {
                nextPartIndex = indexOf;
                nextType = Part.PartType.OF;
                nextLenOfType = 4;
            }
            if (indexName >= 0 && indexName < nextPartIndex || nextPartIndex == -1) {
                nextPartIndex = indexName;
                nextType = Part.PartType.NAME;
                nextLenOfType = 7;
            }
            if (nextPartIndex == -1)
                workPart.setValue(s.substring(startsIndex + lenOfType));
            else
                workPart.setValue(s.substring(startsIndex + lenOfType, nextPartIndex));
            lenOfType = nextLenOfType;
            startsIndex = nextPartIndex;
        }
        return ret;
    }

    static class Part {
        enum PartType {
            MATERIAL,
            OF,
            NAME
        }

        @Getter
        @Setter
        private PartType type;
        @Getter
        private String value;

        public void setValue(String value){
            this.value = value.replaceAll("\\\\of", "of");
        }

        @Override
        public String toString() {
            return "Part [type=" + type + ", value=" + value + "]";
        }

    }

    /**
     * Serialize a object template
     * Requires Jackson FasterXML
     * 
     * @param itemTemplate The item template to serialize
     * @return A string representing the object
     */
    public static String serialize(ObsidianItemTemplate itemTemplate) {
        if (itemTemplate.isPure())
            return MaterialParser.serialize(itemTemplate.getMaterial());
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(itemTemplate);
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return itemTemplate.getMaterial().name();
    }
}
