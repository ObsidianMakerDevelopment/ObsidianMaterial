package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.Arrays;
import java.util.Optional;

import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.inventory.ItemStack;

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

        Arrays.stream(XMaterial.values()).forEach(xMat -> {
            ObsidianMaterial mat = tryParse(xMat.name()).get();
            if(mat instanceof com.moyskleytech.obsidian.material.implementations.XMaterial)
            {
                ObsidianMaterial.add(mat);
            }
        });
    }

    /**
     * Check if XMaterial is functionnal
     * 
     * @return if XMaterial is functionnal
     */
    public static boolean isSupported() {
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
            return Optional.of(
                    new com.moyskleytech.obsidian.material.implementations.XMaterial(xMaterial.get(), materialString));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(ItemStack stack) {
        try {
            return Optional.of(ObsidianMaterial.valueOf(XMaterial.matchXMaterial(stack).name()));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(Block stack) {
        try {
            return Optional.of(ObsidianMaterial.wrap(stack.getType()));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(BlockData stack) {
        try {
            return Optional.of(ObsidianMaterial.wrap(stack.getMaterial()));
        } catch (Throwable t) {
            return Optional.empty();
        }

    }
}
