package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.OraxenMaterial;

import io.th0rgal.oraxen.items.ItemBuilder;
import io.th0rgal.oraxen.items.OraxenItems;

/**
 * Adapter for OraxenAPI
 */
public class OraxenAdapter implements Adapter{

    /**
     * Required constructor that throws if Oraxen is missing
     */
    public OraxenAdapter()
    {
        OraxenItems.getItems().forEach((ib)->{
            ObsidianMaterial.add(new OraxenMaterial(ib));
        });
    }
    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        
        ItemBuilder ib = OraxenItems.getItemById(materialString);
        if(ib!=null)
        {
            return Optional.of(new OraxenMaterial(ib));
        }

        return Optional.empty();
    }
    
}
