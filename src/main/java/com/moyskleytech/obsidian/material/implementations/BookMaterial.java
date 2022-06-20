package com.moyskleytech.obsidian.material.implementations;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

import lombok.Getter;

/**
 * Special implementation of ObsidianMaterial for Enchanted books
 */
public class BookMaterial extends ObsidianMaterial {
    @Getter
    Map<Enchantment, Integer> enchants = new HashMap<>();
    Material mat;

    private static Optional<Boolean> support = Optional.empty();

    /**
     * Validate that the enchant meta data exists on the version of bukkit used
     * @return If Book materials can be used
     */
    public static boolean isSupported() {
        if (support.isPresent())
            return support.get();
        try {
            Class.forName("org.bukkit.inventory.meta.EnchantmentStorageMeta");
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
    public BookMaterial(Map<Enchantment, Integer> effects, String key) {
        super(key);
        this.enchants.putAll(effects);
    }

    @Override
    public Material toMaterial() {
        return mat;
    }

    @Override
    public ItemStack toItem() {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK, 1);

        ItemMeta itemMeta = book.getItemMeta();
        if (itemMeta == null)
            return book;

        if (!(itemMeta instanceof EnchantmentStorageMeta))
            return book;
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) itemMeta;

        for (Entry<Enchantment, Integer> enchantement : enchants.entrySet()) {
            meta.addStoredEnchant(enchantement.getKey(), enchantement.getValue(), true);
        }

        book.setItemMeta(meta);
        return book;
    }
}
