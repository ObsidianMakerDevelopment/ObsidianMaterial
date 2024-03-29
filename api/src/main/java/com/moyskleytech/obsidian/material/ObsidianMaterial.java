package com.moyskleytech.obsidian.material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cryptomorin.xseries.XMaterial;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.moyskleytech.obsidian.material.implementations.*;
import com.moyskleytech.obsidian.material.implementations.adapters.Adapter;
import com.moyskleytech.obsidian.material.implementations.adapters.BookAdapter;
import com.moyskleytech.obsidian.material.implementations.adapters.BukkitAdapter;
import com.moyskleytech.obsidian.material.implementations.adapters.BukkitRegistryAdapter;
import com.moyskleytech.obsidian.material.implementations.adapters.HeadAdapter;
import com.moyskleytech.obsidian.material.implementations.adapters.OraxenAdapter;
import com.moyskleytech.obsidian.material.implementations.adapters.PotionAdapter;
import com.moyskleytech.obsidian.material.implementations.adapters.SkriptAdapter;
import com.moyskleytech.obsidian.material.implementations.adapters.SlimeFunAdapter;
import com.moyskleytech.obsidian.material.implementations.adapters.SpawnerAdapter;
import com.moyskleytech.obsidian.material.implementations.adapters.XMaterialAdapter;
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
    private static List<Adapter> adapters = new ArrayList<>();
    @Getter
    private String key;
    private static boolean need_lazy = true;
    static boolean need_later = true;

    static void lazy() {
        if (need_lazy) {
            need_lazy = false;
            registerAdapter(BookAdapter.class);
            registerAdapter(PotionAdapter.class);
            registerAdapter(SpawnerAdapter.class);

            registerAdapter(BukkitAdapter.class);
            registerAdapter(BukkitRegistryAdapter.class);
            registerAdapter(HeadAdapter.class);

            registerAdapter(XMaterialAdapter.class);
        }
    }

    static public void registerPluginAdapters(Plugin pl) {
        registerAdapter(OraxenAdapter.class, pl);
        registerAdapter(SkriptAdapter.class, pl);
        registerAdapter(SlimeFunAdapter.class, pl);
    }

    /**
     * Class for registering adapters for parsing
     * 
     * @param clazz The class of the adapter, will auto call the parameterless
     *              constructor
     */
    public static void registerAdapter(Class<? extends Adapter> clazz, Plugin pl) {
        lazy();
        try {
            adapters.add(clazz.getDeclaredConstructor(Plugin.class).newInstance(pl));
            Bukkit.getLogger().info("[ObsidianMaterial] Registered " + clazz.getSimpleName());
        } catch (Throwable ignored) {
            Bukkit.getLogger().warning("Could not register " + clazz.getSimpleName() + "::" + ignored.getCause().getMessage());
        }
    }

    /**
     * Class for registering adapters for parsing
     * 
     * @param clazz The class of the adapter, will auto call the parameterless
     *              constructor
     */
    public static void registerAdapter(Class<? extends Adapter> clazz) {
        lazy();
        try {
            adapters.add(clazz.getDeclaredConstructor().newInstance());
            Bukkit.getLogger().info("[ObsidianMaterial] Registered " + clazz.getSimpleName());
        } catch (Throwable ignored) {
            Bukkit.getLogger().warning("Could not register " + clazz.getSimpleName() + "::" + ignored.getCause().getMessage());
        }
    }

    /**
     * Allow to remove a custom implementation of ObsidianMaterial from the cache
     * 
     * @param s The key to remove
     * @return the removed value or null
     */
    public static final ObsidianMaterial remove(String s) {
        lazy();
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
        lazy();
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
        lazy();
        materials.put(im.getKey(), im);
        return im;
    }

    /**
     * Add a custom material into the cache that could be later used with valueOf,
     * should be called for all subclasses
     * 
     * @param im The new material to register
     * @return The registered material
     */
    public static final ObsidianMaterial add(ObsidianMaterial im, String alternateKey) {
        lazy();
        materials.put(alternateKey, im);
        return im;
    }

    /**
     * Wrap a Material into a ObsidianMaterial, same as wrap(org.bukkit.Material)
     * 
     * @param materialString The material to represents
     * @return The ObsidianMaterial associated with it
     */
    public static final ObsidianMaterial valueOf(Material materialString) {
        lazy();
        return valueOf(materialString.getKey().toString());
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
        lazy();
        if (materialString == null)
            return null;
        if (materials.containsKey(materialString)) {
            return materials.get(materialString);
        }

        for (Adapter adap : adapters) {
            try {
                Optional<ObsidianMaterial> mat = adap.tryParse(materialString);
                if (mat.isPresent()) {
                    materials.put(materialString, mat.get());
                    return materials.get(materialString);
                }
            } catch (Throwable t) {

            }
        }

        return null;
    }

    /**
     * Parse a material from a string, currently supports BookMaterial,
     * HeadMaterial, BukkitMaterials, PotionMaterial, SpawnerMaterial and XMaterial,
     * custom implementations use .add()
     * 
     * @param materialString the string to parse
     * @return Corresponding material of null if nothing match
     */
    public static final ObsidianMaterial match(ItemStack materialString) {
        lazy();
        if (materialString == null)
            return null;
        List<Adapter> m = new ArrayList<>(adapters);
        Collections.reverse(m);
        for (Adapter adap : m) {
            try {
                Optional<ObsidianMaterial> mat = adap.tryMatch(materialString);
                if (mat.isPresent())
                    return mat.get();
            } catch (Throwable t) {

            }
        }
        return null;
    }

    /**
     * Parse a material from a string, currently supports BookMaterial,
     * HeadMaterial, BukkitMaterials, PotionMaterial, SpawnerMaterial and XMaterial,
     * custom implementations use .add()
     * 
     * @param materialString the string to parse
     * @return Corresponding material of null if nothing match
     */
    public static final ObsidianMaterial match(Block materialString) {
        lazy();
        if (materialString == null)
            return null;
        List<Adapter> m = new ArrayList<>(adapters);
        Collections.reverse(m);

        for (Adapter adap : m) {
            try {
                Optional<ObsidianMaterial> mat = adap.tryMatch(materialString);
                if (mat.isPresent())
                    return mat.get();
            } catch (Throwable t) {

            }
        }
        return null;
    }

    /**
     * Parse a material from a string, currently supports BookMaterial,
     * HeadMaterial, BukkitMaterials, PotionMaterial, SpawnerMaterial and XMaterial,
     * custom implementations use .add()
     * 
     * @param materialString the string to parse
     * @return Corresponding material of null if nothing match
     */
    public static final ObsidianMaterial match(BlockData materialString) {
        lazy();
        if (materialString == null)
            return null;
        List<Adapter> m = new ArrayList<>(adapters);
        Collections.reverse(m);

        for (Adapter adap : m) {
            try {
                Optional<ObsidianMaterial> mat = adap.tryMatch(materialString);
                if (mat.isPresent())
                    return mat.get();
            } catch (Throwable t) {

            }
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
        lazy();
        return new ArrayList<>(materials.values());
    }

    /**
     * Force all bukkit materials into the cache
     */
    public static void registerAllBukkitMaterials() {
        lazy();
       
        for (PotionType potion : PotionType.values()) {
            valueOf(potion.name() + "_POTION");
            valueOf("EXTENTED_" + potion.name() + "_POTION");
            valueOf(potion.name() + "_2_POTION");
            valueOf("EXTENTED_" + potion.name() + "_2_POTION");
            valueOf(potion.name() + "_SPLASH_POTION");
            valueOf("EXTENTED_" + potion.name() + "_SPLASH_POTION");
            valueOf(potion.name() + "_2_SPLASH_POTION");
            valueOf("EXTENTED_" + potion.name() + "_2_SPLASH_POTION");
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
     * Since ObsidianMaterial can have multiple name for a single material, use the
     * normalized name when requiring unique
     * 
     * @return normalized unique key of the material, might differ from name
     */
    public String normalizedName() {
        ObsidianMaterial normalized = ObsidianMaterial.match(toItem());
        if (normalized != null)
            return normalized.name();
        else
            return name();
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

    /**
     * Set the block to the specified material, useful for plugin that need
     * placement
     * 
     */
    public abstract void setBlock(Block b);

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
