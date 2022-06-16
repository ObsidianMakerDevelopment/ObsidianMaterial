package com.moyskleytech.obsidian.material;

public class MaterialParser {
    public static ObsidianMaterial deserialize(String str)
    {
        return ObsidianMaterial.valueOf(str);
    }
    public static String serialize(ObsidianMaterial itemTemplate)
    {
        return itemTemplate.getKey();
    }
}
