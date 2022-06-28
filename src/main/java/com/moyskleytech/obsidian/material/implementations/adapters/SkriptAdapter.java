package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import org.bukkit.Bukkit;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

/**
 * Adapter for Skript item names
 */
public class SkriptAdapter implements Adapter {

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        if (Bukkit.getPluginManager().isPluginEnabled("Skript")) {
            return Optional.of(ObsidianMaterial.wrap(ch.njol.skript.aliases.Aliases.parseItemType(materialString).getRandom().getType()));
        }
        
        return Optional.empty();
    }
    
}
