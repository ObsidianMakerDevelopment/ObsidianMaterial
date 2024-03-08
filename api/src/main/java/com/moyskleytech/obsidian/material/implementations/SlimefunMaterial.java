package com.moyskleytech.obsidian.material.implementations;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import me.mrCookieSlime.Slimefun.api.BlockStorage;

/**
 * Slimefun4 support material
 */
public class SlimefunMaterial extends ObsidianMaterial {

    SlimefunItem sfi;

    /**
     * Construct a slimefun4 support material
     * 
     * @param item Slimefun format
     */
    public SlimefunMaterial(SlimefunItem item) {
        super(item.getId());
        sfi = item;
    }

    @Override
    public Material toMaterial() {
        return toItem().getType();
    }

    @Override
    public ItemStack toItem() {
        return sfi.getItem();
    }

    @Override
    public void setBlock(Block b) {
        b.setType(sfi.getItem().getType());
        if (Slimefun.getBlockDataService().isTileEntity(b.getType())) {
            Slimefun.getBlockDataService().setBlockData(b, sfi.getId());
        }
        BlockStorage.addBlockInfo(b, "id", sfi.getId(), true);
    }
}
