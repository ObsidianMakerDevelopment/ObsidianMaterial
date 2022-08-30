package com.moyskleytech.obsidian.material;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * Jackson JsonDeserializer for ObsidianMaterial when it's used as a key
 * Requires registerKeyDeserializer to be called on the object mapper before use
 */
public class ObsidianMaterialKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(final String key, final DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        return ObsidianMaterial.valueOf(key);
    }

    /**
     * Register the deserializer with the specified object mapper
     * 
     * @param objectMapper The mapper where to register
     */
    public static void registerKeyDeserializer(ObjectMapper objectMapper) {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addKeyDeserializer(ObsidianMaterial.class, new ObsidianMaterialKeyDeserializer());
        objectMapper.registerModule(simpleModule);
    }
}