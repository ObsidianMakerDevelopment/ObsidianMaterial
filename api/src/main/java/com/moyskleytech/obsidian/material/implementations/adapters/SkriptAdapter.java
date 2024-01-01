package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.SkriptMaterial;

import ch.njol.skript.aliases.Aliases;
import ch.njol.skript.aliases.ItemType;

/**
 * Adapter for Skript item names
 */
public class SkriptAdapter implements Adapter {

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        try {
            if (Bukkit.getPluginManager().isPluginEnabled("Skript")) {
                return Optional.of(new SkriptMaterial(ch.njol.skript.aliases.Aliases.parseItemType(materialString),
                        materialString));
            }
        } catch (Throwable t) {
        }

        return Optional.empty();
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(ItemStack stack) {
        try {
            String materialString = ItemType.toString(stack, 0);
            return Optional.of(new SkriptMaterial(Aliases.parseItemType(materialString), materialString));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(Block stack) {
        try {
            String materialString = ItemType.toString(stack, 0);
            return Optional.of(new SkriptMaterial(Aliases.parseItemType(materialString), materialString));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(BlockData stack) {
        return Optional.empty();
    }
}
