package com.moyskleytech.obsidian.material.implementations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

import ch.njol.skript.aliases.ItemType;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Special implementation of ObsidianMaterial for Skript
 */
public class SkriptMaterial extends ObsidianMaterial {
    ItemType type;
    private static Optional<Boolean> support = Optional.empty();

    /**
     * Validate that the enchant meta data exists on the version of bukkit used
     * @return If Book materials can be used
     */
    public static boolean isSupported() {
        if (support.isPresent())
            return support.get();
        try {
            Class.forName("ch.njol.skript.aliases.ItemType");
            support = Optional.of(true);
        } catch (ClassNotFoundException classError) {
            support = Optional.of(false);
        }
        return support.get();
    }

    /**
     * Create a book material from a list of enchant with associated level
     * @param effects Enchants and level
     * @param key The string representing the material
     */
    public SkriptMaterial(ItemType effects, String key) {
        super(key);
        this.type=effects;
    }

    @Override
    public Material toMaterial() {
        return type.getMaterial();
    }

    @Override
    public ItemStack toItem() {
        return type.getRandom();
    }

    public boolean isSimilar(ItemStack item) {
       return type.isOfType(item);
    }

    @Override
    public void setBlock(Block b) {
       type.getBlock().setBlock(b, true);
    }
}
