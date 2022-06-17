package com.moyskleytech.obsidian.material.parsers;

import java.io.IOException;
import java.util.Map.Entry;

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
            if (template.getMeta()!=null) {
                gen.writeFieldName("meta");
                gen.writeObject(template.getMeta());
            }
            if (template.getEnchants().size() > 0) {
                gen.writeFieldName("enchants");
                gen.writeStartObject();
                for(Entry<Enchantment,Integer> entry: template.getEnchants().entrySet())
                {
                    gen.writeFieldName(entry.getKey().getName());
                    gen.writeNumber(entry.getValue().intValue());
                }
                gen.writeEndObject();
            }
            if (template.getLore().size() > 0) {
                gen.writeFieldName("enchants");
                gen.writeStartArray();
                for (String loreLine : template.getLore()) {
                    gen.writeString(loreLine);
                }
                gen.writeEndArray();
            }

            gen.writeEndObject();
        }
    }

}
