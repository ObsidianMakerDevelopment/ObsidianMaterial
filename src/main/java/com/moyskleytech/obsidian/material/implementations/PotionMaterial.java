package com.moyskleytech.obsidian.material.implementations;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

import lombok.Getter;

public class PotionMaterial extends ObsidianMaterial {
    @Getter
    PotionType type;
    Material mat;

    private static Optional<Boolean> support = Optional.empty();
    private boolean extended;
    private boolean tier2;

    public static boolean isSupported() {
        if (support.isPresent())
            return support.get();
        try {
            Class.forName("org.bukkit.inventory.meta.PotionMeta");
            support = Optional.of(true);
        } catch (ClassNotFoundException classError) {
            support = Optional.of(false);
        }
        return support.get();
    }

    public PotionMaterial(PotionType effects, String key, boolean extended, boolean tier2, boolean splash) {
        super(key);
        this.type = effects;
        this.extended = extended;
        this.tier2 = tier2;
        mat = splash ? Material.SPLASH_POTION : Material.POTION;
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

        PotionMeta pm = (PotionMeta) itemMeta;
        PotionData pd = new PotionData(type, extended, tier2);

        pm.setBasePotionData(pd);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public static ObsidianMaterial getMaterial(ItemStack itemStack) {
        /*
         * boolean isSplash = materialString.contains("_SPLASH");
         * boolean isExtented = materialString.startsWith("EXTENDED_");
         * boolean isTier2 = materialString.contains("_2");
         * String potionString = materialString.replaceAll("_POTION",
         * "").replaceAll("_SPLASH", "").replaceAll("EXTENDED_", "").replaceAll("_2",
         * "");
         */
        /*
         * PotionType t = PotionType.valueOf(potionString);
         * if (t != null) {
         * materials.put(key, new PotionMaterial(t, key, isExtented, isTier2,
         * isSplash));
         * return materials.get(key);
         * }
         */
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return ObsidianMaterial.valueOf(itemStack.getType());
        if (!(itemMeta instanceof PotionMeta))
            return ObsidianMaterial.valueOf(itemStack.getType());
        PotionMeta pm = (PotionMeta) itemMeta;
        // PotionData pd = new PotionData(type,extended,tier2);
        PotionData pd = pm.getBasePotionData();
        String str = pd.getType().toString();
        if(pd.isExtended())
        {
            str = "EXTENTED_"+pd;
        }
        if(pd.isUpgraded())
        {
            str = str +"_2";
        }
        str = str + itemStack.getType().name();
        return ObsidianMaterial.valueOf(str);
    }
}
