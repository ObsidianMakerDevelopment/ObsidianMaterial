package com.moyskleytech.obsidian.material.parsers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.moyskleytech.obsidian.material.ItemParser;
import com.moyskleytech.obsidian.material.ObsidianItemTemplate;

public class ObsidianItemTemplateSerialize extends JsonSerializer<ObsidianItemTemplate> {

    @Override
    public void serialize(ObsidianItemTemplate template, JsonGenerator gen, SerializerProvider arg2)
            throws IOException {
        if (template.isPure())
            gen.writeString(ItemParser.serialize(template));
        else {
            gen.writeStartObject();
            gen.writeFieldName("material");
            gen.writeObject(template.getMaterial());
            if (template.getName() != null) {
                gen.writeFieldName("name");
                gen.writeObject(template.getName());
            }
            if (template.isUnbreakable()) {
                gen.writeFieldName("unbreakable");
                gen.writeObject(template.isUnbreakable());
            }
            if (template.getMeta() != null) {
                gen.writeFieldName("meta");
                gen.writeObject(metaSerialize(template.getMeta()));
            }
            if (template.getEnchants().size() > 0) {
                gen.writeFieldName("enchants");
                gen.writeStartObject();
                for (Entry<Enchantment, Integer> entry : template.getEnchants().entrySet()) {
                    gen.writeFieldName(entry.getKey().getName());
                    gen.writeNumber(entry.getValue().intValue());
                }
                gen.writeEndObject();
            }
            if (template.getLore().size() > 0) {
                gen.writeFieldName("lore");
                gen.writeStartArray();
                for (String loreLine : template.getLore()) {
                    gen.writeString(loreLine);
                }
                gen.writeEndArray();
            }

            gen.writeEndObject();
        }
    }

    public Map<String, Object> metaSerialize(ConfigurationSerializable meta) {
        try {
            Map<String, Object> map = new HashMap<>(meta.serialize());
            String alias = ConfigurationSerialization.getAlias(meta.getClass());
            map.put("==", alias);
            return map;//new SerializedObject(alias, map);
        } catch (Throwable t) {
            t.printStackTrace();
            return null;
        }
    }
}
