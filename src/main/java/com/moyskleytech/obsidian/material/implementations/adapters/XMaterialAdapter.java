package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Optional;

import com.cryptomorin.xseries.XMaterial;
import com.moyskleytech.obsidian.material.ObsidianMaterial;

/**
 * Adapter for XMaterial
 */
public class XMaterialAdapter implements Adapter {
  /**
     * Create a adapter for parsing
     */
    public XMaterialAdapter() {
        if (!isSupported())
            throw new UnsupportedOperationException("XMaterialAdapter isn't available on this server");
    }

      /**
     * Check if XMaterial is functionnal
     * @return  if XMaterial is functionnal
     */
    public static boolean isSupported()
    {
        try {
            com.moyskleytech.obsidian.material.implementations.XMaterial.isSupported();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(materialString);
        if (xMaterial.isPresent()) {
            return Optional.of(new com.moyskleytech.obsidian.material.implementations.XMaterial(xMaterial.get(), materialString));
        }
        return Optional.empty();
    }
    
}
