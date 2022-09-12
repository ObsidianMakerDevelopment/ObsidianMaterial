package com.moyskleytech.obsidian.material.implementations.adapters;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.BukkitMaterial;

/**
 * Adapter for bukkit material
 */
public class BukkitAdapter implements Adapter {

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        materialString = materialString.toUpperCase();

        Material mat = Material.getMaterial(materialString);
        if (mat != null) {
           return Optional.of(new BukkitMaterial(mat, materialString));
        }
        return Optional.empty();
        
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(ItemStack stack) {
        return Optional.of(ObsidianMaterial.valueOf(stack.getType()));
    }

    
}