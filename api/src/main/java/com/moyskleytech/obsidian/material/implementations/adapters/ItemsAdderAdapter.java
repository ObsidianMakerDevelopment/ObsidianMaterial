package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.ItemsAdderMaterial;

import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomFurniture;
import dev.lone.itemsadder.api.CustomStack;

/**
 * Adapter for ItemsAdder API
 */
public class ItemsAdderAdapter implements Adapter {

    /**
     * Required constructor that throws if ItemsAdder is missing
     */
    public ItemsAdderAdapter() {
        CustomBlock cb = new CustomBlock();
        cb.getBaseBlockData();
    }

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {

        CustomBlock cb = CustomBlock.getInstance(materialString);
        if (cb != null) {
            return Optional.of(new ItemsAdderMaterial(cb));
        }
        CustomStack cs = CustomStack.getInstance(materialString);
        if (cs != null) {
            return Optional.of(new ItemsAdderMaterial(cs));
        }
        cs = CustomFurniture.getInstance(materialString);
        if (cs != null) {
            return Optional.of(new ItemsAdderMaterial(cs));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(ItemStack stack) {
        try {
            CustomStack cs = CustomStack.byItemStack(stack);
            if (cs == null)
                cs = CustomFurniture.byItemStack(stack);
            return Optional.of(new ItemsAdderMaterial(cs));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(Block stack) {
        try {
            CustomStack cs = CustomBlock.byAlreadyPlaced(stack);
            return Optional.of(new ItemsAdderMaterial(cs));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(BlockData stack) {
        
        return Optional.empty();
    }

}
