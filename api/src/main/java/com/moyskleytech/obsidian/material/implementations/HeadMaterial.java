package com.moyskleytech.obsidian.material.implementations;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.SkullType;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import com.cryptomorin.xseries.XMaterial;
import com.moyskleytech.obsidian.material.ObsidianMaterial;

import lombok.Getter;

/**
 * Special implementation allowing heads to be stored as material
 */
public class HeadMaterial extends ObsidianMaterial {
    OfflinePlayer ownerPlayer;

    public HeadMaterial with(OfflinePlayer p) {
        return new HeadMaterial(p.getName() + "_HEAD", p);
    }

    public HeadMaterial with(String p) {
        return new HeadMaterial(p + "_HEAD", p);
    }

    public String getOwner() {
        return ownerPlayer.getName();
    }

    public OfflinePlayer getPlayer() {
        return ownerPlayer;
    }

    Material mat;

    private static Optional<Boolean> support = Optional.empty();

    /**
     * Validate that the enchant meta data exists on the version of bukkit used
     * 
     * @return If Head materials can be used
     */
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

    /**
     * Build a Head material for a specified owner
     * 
     * @param key   The unique key
     * @param owner The owner of the head
     */
    public HeadMaterial(String key, String owner) {
        super(key);
        mat = ObsidianMaterial.valueOf("PLAYER_HEAD").toMaterial();
        this.ownerPlayer = Bukkit.getOfflinePlayer(owner);
    }

    /**
     * Build a Head material for a specified owner
     * 
     * @param key   The unique key
     * @param owner The owner of the head
     */
    public HeadMaterial(String key, OfflinePlayer owner) {
        super(key);
        mat = ObsidianMaterial.valueOf("PLAYER_HEAD").toMaterial();
        this.ownerPlayer = owner;
    }

    @Override
    public Material toMaterial() {
        return mat;
    }

    @Override
    public ItemStack toItem() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return itemStack;
        if (!(itemMeta instanceof SkullMeta))
            return itemStack;
        SkullMeta pm = (SkullMeta) itemMeta;

        try {
            m113setOwner(pm);
        } catch (Throwable t) {
            pm.setOwner(getOwner());
        }

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    private void m113setOwner(SkullMeta pm) {
        pm.setOwningPlayer(ownerPlayer);
    }

    /**
     * return the correct ObsidianMaterial for a itemstack of a head
     * 
     * @param itemStack The item stack to serialize
     * @return The correct instance of ObsidianMaterial for the item stack
     */
    public static ObsidianMaterial getMaterial(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return ObsidianMaterial.valueOf("PLAYER_HEAD");
        if (!(itemMeta instanceof SkullMeta))
            return ObsidianMaterial.valueOf("PLAYER_HEAD");
        SkullMeta pm = (SkullMeta) itemMeta;
        return ObsidianMaterial.valueOf(pm.getOwner() + "_HEAD");
    }

    @Override
    public void setBlock(Block b) {
        b.setType(Material.PLAYER_HEAD);
        BlockState state = b.getState();

        if (state instanceof Skull) {
            Skull skull = (Skull) state;

            skull.setOwningPlayer(ownerPlayer);
            skull.update();
        }
    }
}
