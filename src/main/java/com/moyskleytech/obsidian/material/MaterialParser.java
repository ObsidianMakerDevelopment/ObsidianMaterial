package com.moyskleytech.obsidian.material;

public class MaterialParser {
    public static ObsidianMaterial deserialize(String str) {
        try {
            return ObsidianMaterial.valueOf(str);
        } catch (Throwable t) {
            t.printStackTrace();
            return ObsidianMaterial.valueOf("STONE");
        }
    }

    public static String serialize(ObsidianMaterial itemTemplate) {
        return itemTemplate.getKey();
    }
}
