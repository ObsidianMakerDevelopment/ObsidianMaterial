package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.BukkitMaterial;

/**
 * Adapter for bukkit material
 */
public class BukkitRegistryAdapter implements Adapter {

    public BukkitRegistryAdapter() {
        Registry.MATERIAL.forEach(material -> {
            ObsidianMaterial.add(new BukkitMaterial(material, material.getKey().toString()));
        });
    }

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        materialString = materialString.toUpperCase();

        Material mat = Registry.MATERIAL.get(NamespacedKey.fromString(materialString));
        if (mat != null) {
            return Optional.of(new BukkitMaterial(mat, materialString));
        }
        return Optional.empty();

    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(ItemStack stack) {
        return Optional.of(ObsidianMaterial.valueOf(stack.getType()));
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(Block stack) {
        return Optional.of(ObsidianMaterial.valueOf(stack.getType()));
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(BlockData stack) {
        return Optional.of(ObsidianMaterial.valueOf(stack.getMaterial()));
    }

}
