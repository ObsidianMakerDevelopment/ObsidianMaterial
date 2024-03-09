package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.SlimefunMaterial;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * Adapter for Slimefun support
 */
public class SlimeFunAdapter implements Adapter {

    /**
     * Required constructor that throws if Slimefun is missing
     */
    public SlimeFunAdapter(Plugin pl) {

        if (pl != null) {
            Bukkit.getScheduler().runTaskLater(pl, new Runnable() {

                @Override
                public void run() {
                    if (Bukkit.getPluginManager().isPluginEnabled("Slimefun")) {
                        Bukkit.getLogger().info("[ObsidianMaterial] Slimefun found");

                        try {
                            Slimefun.getRegistry().getSlimefunItemIds().forEach((key, item) -> {
                                SlimefunMaterial itm = new SlimefunMaterial(item);
                                ObsidianMaterial.add(itm);
                            });
                        } catch (Throwable t) {
                            Bukkit.getLogger().warning("[ObsidianMaterial] Slimefun error" + t.getMessage());
                        }
                    }
                }

            }, 20);

        }

    }

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        if (materialString.startsWith("slimefun:"))
            materialString = materialString.replace("slimefun:", "");
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
        SlimefunItem maybe = BlockStorage.check(stack);
        if (maybe != null) {
            return Optional.of(new SlimefunMaterial(maybe));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(BlockData stack) {
        return Optional.empty();
    }
}
