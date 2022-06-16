package com.moyskleytech.obsidian.material.implementations;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

import lombok.Getter;

public class BukkitMaterial extends ObsidianMaterial {
    @Getter
    Material mat;

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

}
