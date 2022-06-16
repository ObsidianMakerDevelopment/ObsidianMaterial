package com.moyskleytech.obsidian.material.implementations;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

import lombok.Getter;

public class XMaterial extends ObsidianMaterial {
    
    @Getter
    private com.cryptomorin.xseries.XMaterial mat;
    public XMaterial(com.cryptomorin.xseries.XMaterial x,String key) {
        super(key);
        this.mat = x;
    }

    @Override
    public Material toMaterial() {
        return mat.parseMaterial();
    }

    @Override
    public ItemStack toItem() {
        ItemStack is = mat.parseItem();
        return is;
    }
    
}
