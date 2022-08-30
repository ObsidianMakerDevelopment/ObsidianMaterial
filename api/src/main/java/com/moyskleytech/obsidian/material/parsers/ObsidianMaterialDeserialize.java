package com.moyskleytech.obsidian.material.parsers;

import java.io.IOException;

import org.bukkit.Bukkit;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.moyskleytech.obsidian.material.MaterialParser;
import com.moyskleytech.obsidian.material.ObsidianMaterial;

/**
 * Jackson JsonDeserializer for ObsidianMaterial
 */
public class ObsidianMaterialDeserialize extends JsonDeserializer<ObsidianMaterial> {
    @Override
    public ObsidianMaterial deserialize(JsonParser arg0, DeserializationContext arg1)
            throws IOException, JacksonException {
        return MaterialParser.deserialize(arg0.getText());
    }
}