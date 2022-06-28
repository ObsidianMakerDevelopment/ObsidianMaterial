package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import org.bukkit.entity.EntityType;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.SpawnerMaterial;

/**
 * Adapter for spawners
 */
public class SpawnerAdapter implements Adapter {

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
    
}
