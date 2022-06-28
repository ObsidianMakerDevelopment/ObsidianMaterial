package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import com.cryptomorin.xseries.XMaterial;
import com.moyskleytech.obsidian.material.ObsidianMaterial;

/**
 * Adapter for XMaterial
 */
public class XMaterialAdapter implements Adapter {

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(materialString);
        if (xMaterial.isPresent()) {
            return Optional.of(new com.moyskleytech.obsidian.material.implementations.XMaterial(xMaterial.get(), materialString));
        }
        return Optional.empty();
    }
    
}
