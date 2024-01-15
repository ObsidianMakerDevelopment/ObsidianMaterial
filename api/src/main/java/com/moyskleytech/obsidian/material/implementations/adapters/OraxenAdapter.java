package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.OraxenMaterial;

import ch.njol.skript.config.Option;
import io.th0rgal.oraxen.items.ItemBuilder;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.api.OraxenBlocks;

/**
 * Adapter for OraxenAPI
 */
public class OraxenAdapter implements Adapter {

    /**
     * Required constructor that throws if Oraxen is missing
     */
    public OraxenAdapter() {
        OraxenItems.getItems().forEach((ib) -> {
            OraxenMaterial oraxenMat = new OraxenMaterial(ib);
            ObsidianMaterial.add(oraxenMat);
            ObsidianMaterial.add(oraxenMat, OraxenItems.getIdByItem(ib));
        });
    }

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {

        ItemBuilder ib = OraxenItems.getItemById(materialString);
        if (ib != null) {
            return Optional.of(new OraxenMaterial(ib));
        }

        return Optional.empty();
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(ItemStack stack) {
        return Optional.ofNullable(ObsidianMaterial.valueOf(OraxenItems.getIdByItem(stack)));
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(Block stack) {
        return Optional.ofNullable(ObsidianMaterial.valueOf(OraxenBlocks.getOraxenBlock(stack.getLocation()).getItemID()));
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(BlockData stack) {
        return Optional.ofNullable(ObsidianMaterial.valueOf(OraxenBlocks.getOraxenBlock(stack).getItemID()));
    }

}
