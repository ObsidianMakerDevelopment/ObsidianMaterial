package com.moyskleytech.obsidian.material;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

public class ObsidianMaterialKeyDeserializer extends KeyDeserializer
{
    @Override
    public Object deserializeKey(final String key, final DeserializationContext ctxt ) throws IOException, JsonProcessingException
    {
        return ObsidianMaterial.valueOf(key);
    }

    public static void registerKeyDeserializer(ObjectMapper objectMapper)
    {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addKeyDeserializer(ObsidianMaterial.class, new ObsidianMaterialKeyDeserializer());
        objectMapper.registerModule(simpleModule);
    }
}