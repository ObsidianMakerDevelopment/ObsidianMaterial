package com.moyskleytech.obsidian.material.parsers;

import java.io.IOException;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.moyskleytech.obsidian.material.ItemParser;
import com.moyskleytech.obsidian.material.ObsidianItemTemplate;

public class ObsidianItemTemplateDeserialize extends JsonDeserializer<ObsidianItemTemplate> {
    @Override
    public ObsidianItemTemplate deserialize(JsonParser arg0, DeserializationContext arg1)
            throws IOException, JacksonException {
        return ItemParser.deserialize(arg0.readValueAs(String.class));
    }
}