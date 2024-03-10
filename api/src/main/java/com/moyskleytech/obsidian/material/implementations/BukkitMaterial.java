package com.moyskleytech.obsidian.material.implementations;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

import lombok.Getter;

/**
 * Special implementation that support all present and future bukkit Material values
 */
public class BukkitMaterial extends ObsidianMaterial {
    @Getter
    Material mat;

    /**
     * Build a ObsidianMaterial around a bukkit material
     * @param mat The bukkit Material to encapsulate
     * @param key The unique key
     */
    public BukkitMaterial(Material mat,String key) {
        super(key);
        this.mat = mat;
    }

    @Override
    public Material toMaterial() {
        return mat;
    }

    @Override
    public ItemStack toItem() {
        ItemStack is = new ItemStack(mat);
        return is;
    }
    @Override
    public void setBlock(Block b) {
        b.setType(mat);
    }

    @Override
    public String normalizedName() {
        try{
            return mat.getKey().toString();
        }
        catch(Throwable t)
        {
            
            return super.normalizedName();
        }
    }
}
