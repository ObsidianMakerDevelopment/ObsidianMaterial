package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import com.moyskleytech.obsidian.material.ObsidianMaterial;

import dev.lone.itemsadder.api.CustomBlock;

/**
 * Adapter for ItemsAdder API
 */
public class ItemsAdderAdapter implements Adapter {

    /**
     * Required constructor that throws if ItemsAdder is missing
     */
    public ItemsAdderAdapter()
    {
        CustomBlock cb  = new CustomBlock();
        cb.getBaseBlockData();
    }
    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
    
        CustomBlock cs = CustomBlock.getInstance(materialString);
        if(cs!=null)
        {

        }
        return Optional.empty();
    }
    
}
