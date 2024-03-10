package com.moyskleytech.obsidian.material.implementations.adapters;

import java.lang.reflect.Method;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.BukkitMaterial;

/**
 * Adapter for bukkit material
 */
public class BukkitAdapter implements Adapter {

    public BukkitAdapter() {
        boolean hasGetKey = methodExists(Material.class, "getKey");
        for (Material mat : Material.values()) {
            ObsidianMaterial.add(new BukkitMaterial(mat, mat.name()));
            if(hasGetKey)
                ObsidianMaterial.add(new BukkitMaterial(mat, mat.getKey().toString()));
            else
                ObsidianMaterial.add(new BukkitMaterial(mat, "minecraft:"+mat.name().toLowerCase()));
        }
        
    }
    public static boolean methodExists(Class clazz, String methodName) {
        boolean result = false;
        for (Method method : clazz.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        materialString = materialString.toUpperCase();

        Material mat = Material.getMaterial(materialString);
        if (mat != null) {
            return Optional.of(new BukkitMaterial(mat, "minecraft:"+materialString.toLowerCase()));
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
