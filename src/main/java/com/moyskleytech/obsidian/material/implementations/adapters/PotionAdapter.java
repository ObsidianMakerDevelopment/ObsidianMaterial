package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import org.bukkit.potion.PotionType;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.PotionMaterial;

/**
 * Adapter for potions
 */
public class PotionAdapter implements Adapter {
  /**
     * Create a adapter for parsing
     */
    public PotionAdapter() {
        if (!PotionMaterial.isSupported())
            throw new UnsupportedOperationException("PotionMaterial isn't available on this server");
    }

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        materialString = materialString.toUpperCase();

        if (materialString.endsWith("_POTION") && PotionMaterial.isSupported()) {
            boolean isSplash = materialString.contains("_SPLASH");
            boolean isExtented = materialString.startsWith("EXTENDED_");
            boolean isTier2 = materialString.contains("_2");
            String potionString = materialString.replaceAll("_POTION", "").replaceAll("_SPLASH", "")
                    .replaceAll("EXTENDED_", "").replaceAll("_2", "");
            try {
                PotionType t = PotionType.valueOf(potionString);
                if (t != null) {
                    return Optional.of(new PotionMaterial(t, materialString, isExtented, isTier2, isSplash));
                }
            } catch (IllegalArgumentException noPotionException) {
                // Just ignore it and try parsing it with XMaterial instead
            }
        }
        return Optional.empty();
    }

    
}
