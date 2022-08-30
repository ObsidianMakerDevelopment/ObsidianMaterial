package com.moyskleytech.obsidian.material.implementations.adapters;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.moyskleytech.obsidian.material.ObsidianMaterial;
import com.moyskleytech.obsidian.material.implementations.BookMaterial;

/**
 * Adapter for BookMaterial
 */
public class BookAdapter implements Adapter {

    /**
     * Create a adapter for parsing
     */
    public BookAdapter() {
        if (!BookMaterial.isSupported())
            throw new UnsupportedOperationException("BookMaterial isn't available on this server");
    }

    @Override
    public Optional<ObsidianMaterial> tryParse(String materialString) {
        materialString = materialString.toUpperCase();

        if (materialString.endsWith("_BOOK") && BookMaterial.isSupported()) {
            // ARROW_FIRE_AND_CHANNELING_BOOK
            String bookString = materialString.replaceAll("_BOOK", "");
            Map<Enchantment, Integer> enchants = new HashMap<>();
            for (String entry : bookString.split("_AND_")) {
                int level = 1;
                String[] elements = entry.split("_");
                boolean isNumeric = elements[elements.length - 1].matches("-?\\d+?");
                if (isNumeric) {
                    entry = entry.substring(0, entry.lastIndexOf("_"));
                    level = Integer.parseInt(elements[elements.length - 1]);
                }

                Enchantment e = null;
                for (Enchantment ect : Enchantment.values()) {
                    if (entry.equalsIgnoreCase(ect.getName()))
                        e = ect;
                }
                if (e != null) {
                    enchants.put(e, level);
                }
            }
            return Optional.of(new BookMaterial(enchants, materialString));
        }
        return Optional.empty();
    }

    @Override
    public Optional<ObsidianMaterial> tryMatch(ItemStack stack) {
        try {
            return Optional.of(BookMaterial.getMaterial(stack));
        } catch (Throwable t) {
            return Optional.empty();
        }
    }

}
