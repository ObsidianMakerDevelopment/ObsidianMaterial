package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Skull;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.HeadMaterial;

/**
 * Adapter for heads
 */
public class HeadAdapter implements Adapter {

    /**
     * Create a adapter for parsing
     */
    public HeadAdapter() {
        if (!HeadMaterial.isSupported())
            throw new UnsupportedOperationException("HeadMaterial isn't available on this server");
    }

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        if (materialString.endsWith("_HEAD") && !materialString.equals("PLAYER_HEAD") && HeadMaterial.isSupported()) {
            String entityString = materialString.replaceAll("_HEAD", "");
            try {
                return Optional.of(new HeadMaterial(materialString, entityString));
            } catch (IllegalArgumentException noEntityException) {
                // Just ignore it and try parsing it with XMaterial instead
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(ItemStack stack) {
        try {
            if (stack.getType().toString().contains("HEAD")) {
                return Optional.of(HeadMaterial.getMaterial(stack));
            }
        } catch (Throwable t) {

        }
        return Optional.empty();
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(Block stack) {
        if(stack.getType() == Material.PLAYER_HEAD)
        {
            BlockState bs = stack.getState();
            Skull skull = (Skull)bs;
            return Optional.of(new HeadMaterial(stack.getType().toString(),skull.getOwningPlayer()));
        }
        if(stack.getType() == Material.PLAYER_WALL_HEAD)
        {
            BlockState bs = stack.getState();
            Skull skull = (Skull)bs;
            return Optional.of(new HeadMaterial(stack.getType().toString(),skull.getOwningPlayer()));
        }
        // TODO: technically you can place a head
        return Optional.empty();
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(BlockData stack) {
        return Optional.empty();
    }

}
