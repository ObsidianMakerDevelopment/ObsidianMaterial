package com.moyskleytech.obsidian.material;
/**
 * Material parser
 */
public class MaterialParser {
    /**
     * Equivalent to ObsidianMaterial.valueOf with a try/catch
     * Return STONE if the material is missing
     * @param str String representing the material
     * @return ObsidianMaterial 
     */
    public static ObsidianMaterial deserialize(String str) {
        try {
            return ObsidianMaterial.valueOf(str);
        } catch (Throwable t) {
            t.printStackTrace();
            return ObsidianMaterial.valueOf("STONE");
        }
    }

    /**
     * Serialize a material, always equal to .getKey()
     * @param itemTemplate return a representation of the material
     * @return A string representing the object
     */
    public static String serialize(ObsidianMaterial itemTemplate) {
        return itemTemplate.getKey();
    }
}
