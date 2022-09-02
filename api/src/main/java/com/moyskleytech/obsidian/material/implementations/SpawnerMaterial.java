package com.moyskleytech.obsidian.material.implementations;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

import lombok.Getter;

/**
 * Special implementation of ObsidianMaterial to allow Spawners with entity
 * types
 */
public class SpawnerMaterial extends ObsidianMaterial {
    @Getter
    EntityType entity;

    private static Optional<Boolean> support = Optional.empty();

    /**
     * Validate that the enchant meta data exists on the version of bukkit used
     * 
     * @return If Spawner materials can be used
     */
    public static boolean isSupported() {
        if (support.isPresent())
            return support.get();
        try {
            Class.forName("org.bukkit.inventory.meta.BlockStateMeta");
            support = Optional.of(true);
        } catch (ClassNotFoundException classError) {
            support = Optional.of(false);
        }
        return support.get();
    }

    /**
     * Build a Spawner material for a specified entity
     * 
     * @param entity The entity to spawn
     * @param key    The key representing the spawner
     */
    public SpawnerMaterial(EntityType entity, String key) {
        super(key);
        this.entity = entity;
    }

    @Override
    public Material toMaterial() {
        return Material.SPAWNER;
    }

    @Override
    public ItemStack toItem() {
        ItemStack itemStack = new ItemStack(Material.SPAWNER);
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return itemStack;
        if (!(itemMeta instanceof BlockStateMeta))
            return itemStack;
        BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
        CreatureSpawner creatureSpawner = (CreatureSpawner) blockStateMeta.getBlockState();
        try {
            creatureSpawner.setSpawnedType(entity);
            } catch (Throwable t) {

        }
        blockStateMeta.setDisplayName(creatureSpawner.getSpawnedType()+" Spawner");
        blockStateMeta.setBlockState(creatureSpawner);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * Object the right spawner material from a spawner itemstack
     * 
     * @param itemStack A item stack containing a spawner
     * @return The correct Material for it
     */
    public static ObsidianMaterial getMaterial(ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null)
            return ObsidianMaterial.valueOf(itemStack.getType().name());
        if (!(itemMeta instanceof BlockStateMeta))
            return ObsidianMaterial.valueOf(itemStack.getType().name());
        BlockStateMeta blockStateMeta = (BlockStateMeta) itemMeta;
        CreatureSpawner creatureSpawner = (CreatureSpawner) blockStateMeta.getBlockState();
        return ObsidianMaterial.valueOf(creatureSpawner.getSpawnedType().name() + "_SPAWNER");
    }
}
