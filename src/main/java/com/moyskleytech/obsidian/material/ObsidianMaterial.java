package com.moyskleytech.obsidian.material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cryptomorin.xseries.XMaterial;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.moyskleytech.obsidian.material.implementations.*;
import com.moyskleytech.obsidian.material.parsers.*;

/**
 * A future proof Material wrapper for bukkit servers allowing all
 * BukkitMaterial without recompiling. Fallsback to XSeries.XMaterial on legacy
 * versions.
 */
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = ObsidianMaterialSerialize.class)
@JsonDeserialize(using = ObsidianMaterialDeserialize.class)
public abstract class ObsidianMaterial implements Comparable<ObsidianMaterial> {
    private static Map<String, ObsidianMaterial> materials = new HashMap<>();

    @Getter
    private String key;

    /**
     * Allow to remove a custom implementation of ObsidianMaterial from the cache
     * @param s The key to remove
     * @return the removed value or null
     */
    public static final ObsidianMaterial remove(String s) {
        ObsidianMaterial im = materials.get(s);
        materials.remove(s);
        return im;
    }

    /**
     * Wrap a bukkit Material into a ObsidianMaterial
     * 
     * @param mat the bukkit Material to wrap
     * @return Returns a bukkit Material representing the value, instanceOf
     *         BukkitMaterial
     */
    public static final ObsidianMaterial wrap(Material mat) {
        return valueOf(mat.name());
    }

    /**
     * Wrap a XMaterial into a ObsidianMaterial
     * 
     * @param mat the XMaterial object
     * @return a ObsidianMaterial representing the object could be instance of
     *         BukkitMaterial or XMaterial
     */
    public static final ObsidianMaterial wrap(XMaterial mat) {
        return valueOf(mat.name());
    }

    /**
     * Add a custom material into the cache that could be later used with valueOf,
     * should be called for all subclasses
     * 
     * @param im The new material to register
     * @return The registered material
     */
    public static final ObsidianMaterial add(ObsidianMaterial im) {
        materials.put(im.getKey(), im);
        return im;
    }

    /**
     * Wrap a Material into a ObsidianMaterial, same as wrap(org.bukkit.Material)
     * 
     * @param materialString The material to represents
     * @return The ObsidianMaterial associated with it
     */
    public static final ObsidianMaterial valueOf(Material materialString) {
        return valueOf(materialString.name());
    }

    /**
     * Parse a material from a string, currently supports BookMaterial,
     * HeadMaterial, BukkitMaterials, PotionMaterial, SpawnerMaterial and XMaterial,
     * custom implementations use .add()
     * 
     * @param materialString the string to parse
     * @return Corresponding material of null if nothing match
     */
    public static final ObsidianMaterial valueOf(String materialString) {
        if (materialString == null)
            return null;
        String key = materialString;
        if (materials.containsKey(key)) {
            return materials.get(key);
        }

        materialString = materialString.toUpperCase();

        Material mat = Material.getMaterial(materialString);
        if (mat != null) {
            materials.put(key, new BukkitMaterial(mat, key));
            return materials.get(key);
        }
        if (materialString.endsWith("_HEAD") && HeadMaterial.isSupported()) {
            String entityString = materialString.replaceAll("_HEAD", "");
            try {
                materials.put(key, new HeadMaterial(key, entityString));
                return materials.get(key);
            } catch (IllegalArgumentException noEntityException) {
                // Just ignore it and try parsing it with XMaterial instead
            }
        }
        if (materialString.endsWith("_SPAWNER") && SpawnerMaterial.isSupported()) {
            String entityString = materialString.replaceAll("_SPAWNER", "");
            try {
                EntityType t = EntityType.valueOf(entityString);
                if (t != null) {
                    materials.put(key, new SpawnerMaterial(t, key));
                    return materials.get(key);
                }
            } catch (IllegalArgumentException noEntityException) {
                // Just ignore it and try parsing it with XMaterial instead
            }
        }
        if (materialString.endsWith("_POTION") && PotionMaterial.isSupported()) {
            boolean isSplash = materialString.contains("_SPLASH");
            boolean isExtented = materialString.startsWith("EXTENDED_");
            boolean isTier2 = materialString.contains("_2");
            String potionString = materialString.replaceAll("_POTION", "").replaceAll("_SPLASH", "")
                    .replaceAll("EXTENDED_", "").replaceAll("_2", "");
            try {
                PotionType t = PotionType.valueOf(potionString);
                if (t != null) {
                    materials.put(key, new PotionMaterial(t, key, isExtented, isTier2, isSplash));
                    return materials.get(key);
                }
            } catch (IllegalArgumentException noPotionException) {
                // Just ignore it and try parsing it with XMaterial instead
            }
        }
        if (materialString.endsWith("_BOOK") && BookMaterial.isSupported()) {
            // ARROW_FIRE_AND_CHANNELING_BOOK
            String bookString = materialString.replaceAll("_BOOK", "");
            Map<Enchantment, Integer> enchants = new HashMap<>();
            for (String entry : bookString.split("_AND_")) {
                int level = 1;
                String[] elements = entry.split("_");
                boolean isNumeric = elements[elements.length - 1].matches("-?\\d+?");
                if (isNumeric) {
                    entry = entry.substring(0, entry.lastIndexOf("_"));
                    level = Integer.parseInt(elements[elements.length - 1]);
                }
                Enchantment e = Enchantment.getByName(entry);
                if (e != null) {
                    enchants.put(e, level);
                }
            }
            materials.put(key, new BookMaterial(enchants, key));
            return materials.get(key);
        }
        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(materialString);
        if (xMaterial.isPresent()) {
            materials.put(key, new com.moyskleytech.obsidian.material.implementations.XMaterial(xMaterial.get(), key));
            return materials.get(key);
        }

        return null;
    }

    /**
     * Return the full list of known material, might be incomplete as Materials are
     * only cached when used
     * 
     * @return All the materials in the cache
     */
    public static List<ObsidianMaterial> values() {
        return new ArrayList<>(materials.values());
    }

    /**
     * Force all bukkit materials into the cache
     */
    public static void registerAllBukkitMaterials() {
        for (Material mat : Material.values()) {
            valueOf(mat.name());
        }
        for (EntityType entity : EntityType.values()) {
            valueOf(entity.name() + "_SPAWNER");
        }
        for (PotionType potion : PotionType.values()) {
            valueOf(potion.name() + "_POTION");
            valueOf("EXTENTED_"+potion.name() + "_POTION");
            valueOf(potion.name() + "_2_POTION");
            valueOf("EXTENTED_"+potion.name() + "_2_POTION");
            valueOf(potion.name() + "_SPLASH_POTION");
            valueOf("EXTENTED_"+potion.name() + "_SPLASH_POTION");
            valueOf(potion.name() + "_2_SPLASH_POTION");
            valueOf("EXTENTED_"+potion.name() + "_2_SPLASH_POTION");
        }
        for(XMaterial xmat: XMaterial.values())
        {
            valueOf(xmat.name());
        }
    }

    /**
     * Make ObsidianMaterial act like a enum, return the key representing this
     * unique instance
     * 
     * @return the unique key of the material
     */
    public String name() {
        return key;
    }

    /**
     * Compare this value with a ItemStack for similarities
     * 
     * @param item The item to compare
     * @return If the itemstack is similar
     */
    public boolean isSimilar(ItemStack item) {
        return toItem().isSimilar(item);
    }

    /**
     * Obtain the bukkit material associated with this material
     * 
     * @return the enum value associated
     */
    public abstract Material toMaterial();

    /**
     * Object a ItemStack of the desired material
     * 
     * @return new ItemStack
     */
    public abstract ItemStack toItem();

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ObsidianMaterial))
            return false;

        return name().equals(((ObsidianMaterial) obj).name());
    }

    @Override
    public int hashCode() {
        return name().hashCode();
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public int compareTo(ObsidianMaterial arg0) {
        return name().compareTo(((ObsidianMaterial) arg0).name());
    }
}
