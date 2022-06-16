package com.moyskleytech.obsidian.material.parsers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.moyskleytech.obsidian.material.ItemParser;
import com.moyskleytech.obsidian.material.ObsidianItemTemplate;

public class ObsidianItemTemplateSerialize  extends JsonSerializer<ObsidianItemTemplate> {

    @Override
    public void serialize(ObsidianItemTemplate arg0, JsonGenerator arg1, SerializerProvider arg2) throws IOException {
       arg1.writeString(ItemParser.serialize(arg0));
    }
    
}
