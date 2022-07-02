package com.moyskleytech.obsidian.material.implementations;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

import dev.lone.itemsadder.api.CustomBlock;

/**
 * ItemsAdder support
 */
public class ItemsAdderMaterial extends ObsidianMaterial {

    private CustomBlock cb;

    /**
     * Build a ItemsAdder support material
     * @param cb ItemsAdder format
     */
    public ItemsAdderMaterial(CustomBlock cb)
    {
        super(cb.getNamespacedID());
        this.cb = cb;
    }
    @Override
    public Material toMaterial() {
        return toItem().getType();
    }

    @Override
    public ItemStack toItem() {
        return cb.getItemStack();
    }
    
}
