package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

/**
 * Adapter class for parsing, do not implement
 */
public interface Adapter {
    /**
     * Try parsing a string as material using the specified adapter
     * @param materialString The String to parse
     * @return Maybe the material
     */
    Optional<ObsidianMaterial> tryParse(String materialString);
    Optional<ObsidianMaterial> tryMatch(ItemStack stack);
}
