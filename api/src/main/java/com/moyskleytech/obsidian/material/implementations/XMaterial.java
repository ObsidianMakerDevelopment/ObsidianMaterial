package com.moyskleytech.obsidian.material.implementations;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

import lombok.Getter;

/**
 * A special wrapper around XSeries's XMaterial that will only be used on legacy servers where some Materials are named differently in org.bukkit.Material
 */
public class XMaterial extends ObsidianMaterial {

    /**
     * Validate XMaterial to be functional
     * @return true or Throws
     */
    public static boolean isSupported()
    {
        com.cryptomorin.xseries.XMaterial mat= com.cryptomorin.xseries.XMaterial.ACACIA_BOAT;
        mat.toString();
        return true;
    }
    @Getter
    private com.cryptomorin.xseries.XMaterial mat;

    /**
     * Build a wrapper around a cryotomorin XMaterial
     * @param x The XMaterial value
     * @param key the String representing it
     */
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
