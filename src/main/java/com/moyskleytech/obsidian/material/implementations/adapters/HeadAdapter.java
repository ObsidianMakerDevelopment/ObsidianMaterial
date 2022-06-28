package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.HeadMaterial;

/**
 * Adapter for heads
 */
public class HeadAdapter implements Adapter {
    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        if (materialString.endsWith("_HEAD") && !materialString.equals("PLAYER_HEAD") && HeadMaterial.isSupported()) {
            String entityString = materialString.replaceAll("_HEAD", "");
            try {
                return Optional.of(new HeadMaterial(materialString, entityString));
            } catch (IllegalArgumentException noEntityException) {
                // Just ignore it and try parsing it with XMaterial instead
            }
        }
        return Optional.empty();
    }
}
