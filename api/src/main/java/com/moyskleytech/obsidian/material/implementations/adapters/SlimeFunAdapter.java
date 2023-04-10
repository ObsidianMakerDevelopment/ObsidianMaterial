package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.SlimefunMaterial;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;

/**
 * Adapter for Slimefun support
 */
public class SlimeFunAdapter implements Adapter {

    /**
     * Required constructor that throws if Slimefun is missing
     */
    public SlimeFunAdapter() {
        SlimefunItem.getById("AIR");
    }

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        SlimefunItem item = SlimefunItem.getById(materialString);
        if (item != null) {
            return Optional.of(new SlimefunMaterial(item));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(ItemStack stack) {
        try {
            return Optional.of(new SlimefunMaterial(SlimefunItem.getByItem(stack)));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(Block stack) {
        return Optional.empty();
    }
}
