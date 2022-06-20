package com.moyskleytech.obsidian.material.implementations;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import com.cryptomorin.xseries.XMaterial;
import com.moyskleytech.obsidian.material.ObsidianMaterial;

import lombok.Getter;

public class HeadMaterial extends ObsidianMaterial {
    @Getter
    String owner;
    Material mat;

    private static Optional<Boolean> support = Optional.empty();

    public static boolean isSupported() {
        if (support.isPresent())
            return support.get();
        try {
            Class.forName("org.bukkit.inventory.meta.SkullMeta");
            support = Optional.of(true);
        } catch (ClassNotFoundException classError) {
            support = Optional.of(false);
        }
        return support.get();
    }

    public HeadMaterial(String key, String owner) {
        super(key);
        mat = XMaterial.PLAYER_HEAD.parseMaterial();
        this.owner = owner;
    }

    @Override
    public Material toMaterial() {
        return mat;
    }

    @Override
    public ItemStack toItem() {
        ItemStack itemStack = new ItemStack(mat);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return itemStack;

        SkullMeta pm = (SkullMeta) itemMeta;
        pm.setOwner(owner);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ObsidianMaterial getMaterial(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return ObsidianMaterial.wrap(XMaterial.PLAYER_HEAD);

        SkullMeta pm = (SkullMeta) itemMeta;
        return ObsidianMaterial.valueOf(pm.getOwner()+"_HEAD");
    }
}
