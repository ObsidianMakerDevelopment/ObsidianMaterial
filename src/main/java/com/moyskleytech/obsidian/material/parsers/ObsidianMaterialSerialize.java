package com.moyskleytech.obsidian.material.parsers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.moyskleytech.obsidian.material.MaterialParser;
import com.moyskleytech.obsidian.material.ObsidianMaterial;

public class ObsidianMaterialSerialize  extends JsonSerializer<ObsidianMaterial> {

    @Override
    public void serialize(ObsidianMaterial arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
       arg1.writeString(MaterialParser.serialize(arg0));
    }
    
}
