package com.moyskleytech.obsidian.material.implementations;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import io.th0rgal.oraxen.api.OraxenItems;
import io.th0rgal.oraxen.api.OraxenBlocks;
import io.th0rgal.oraxen.items.ItemBuilder;
/**
 * Oraxen support material
 */
public class OraxenMaterial extends ObsidianMaterial {

    ItemBuilder oraxenItem;
    /**
     * Build a oraxen support material
     * @param ib Oraxen format
     */
    public OraxenMaterial(ItemBuilder ib) {
        super("oraxen:"+OraxenItems.getIdByItem(ib));
        oraxenItem=ib;
    }

    @Override
    public Material toMaterial() {
        return toItem().getType();
    }

    @Override
    public ItemStack toItem() {
        return oraxenItem.build();
    }

    @Override
    public void setBlock(Block b) {
        OraxenBlocks.place(OraxenItems.getIdByItem(oraxenItem), b.getLocation());
    }
}
