package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.SpawnerMaterial;

/**
 * Adapter for spawners
 */
public class SpawnerAdapter implements Adapter {
    /**
     * Create a adapter for parsing
     */
    public SpawnerAdapter() {
        if (!SpawnerMaterial.isSupported())
            throw new UnsupportedOperationException("SpawnerMaterial isn't available on this server");
    }

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        materialString = materialString.toUpperCase();

        if (materialString.endsWith("_SPAWNER") && SpawnerMaterial.isSupported()) {
            String entityString = materialString.replaceAll("_SPAWNER", "");
            try {
                EntityType t = EntityType.valueOf(entityString);
                if (t != null) {
                    return Optional.of(new SpawnerMaterial(t, materialString));
                }
            } catch (IllegalArgumentException noEntityException) {
                // Just ignore it and try parsing it with XMaterial instead
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(ItemStack stack) {
        try {
            if (stack.getType().toString().contains("SPAWNER")) {
                return Optional.of(SpawnerMaterial.getMaterial(stack));
            }
            return Optional.empty();
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(Block stack) {
        BlockState bs = stack.getState();
        if (bs != null) {
            try {
                if (bs instanceof CreatureSpawner) {
                    CreatureSpawner spawner = (CreatureSpawner) bs;
                    return Optional.of(ObsidianMaterial.valueOf(spawner.getSpawnedType() + "_SPAWNER"));
                }
            } catch (Throwable t) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
    @Override
    public Optional<ObsidianMaterial> tryMatch(BlockData stack) {
       return Optional.empty();
    }

}