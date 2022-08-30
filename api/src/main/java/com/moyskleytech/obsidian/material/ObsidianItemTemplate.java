package com.moyskleytech.obsidian.material;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.cryptomorin.xseries.XMaterial;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.moyskleytech.obsidian.material.ItemParser.Part;
import com.moyskleytech.obsidian.material.ItemParser.Part.PartType;
import com.moyskleytech.obsidian.material.implementations.PotionMaterial;
import com.moyskleytech.obsidian.material.implementations.SpawnerMaterial;
import com.moyskleytech.obsidian.material.parsers.ObsidianItemTemplateDeserialize;
import com.moyskleytech.obsidian.material.parsers.ObsidianItemTemplateSerialize;

import lombok.Getter;

/**
 * Represents a item template
 */
@JsonSerialize(using = ObsidianItemTemplateSerialize.class)
@JsonDeserialize(using = ObsidianItemTemplateDeserialize.class)
public class ObsidianItemTemplate {

    /**
     * The material
     */
    @Getter
    private ObsidianMaterial material;

    /**
     * Lines in the lore
     */
    private List<String> lore = new ArrayList<>();
    /**
     * Enchants of the object
     */
    private Map<Enchantment, Integer> enchants = new HashMap<>();
    /**
     * The display name of the object
     */
    @Getter
    private String name;
    /**
     * The display name of the object
     */
    @Getter
    private short durability;
    /**
     * The unbreakable flag
     */
    @Getter
    private boolean unbreakable;
    /**
     * The meta information for advanced items such as spawners and skull
     */
    @Getter
    private ItemMeta meta;

    /**
     * Create a base template containing STONE material
     */
    public ObsidianItemTemplate() {
        material = ObsidianMaterial.valueOf("STONE");
    }

    /**
     * Create a material from a legacy string
     * 
     * @param parse Legacy String
     */
    public ObsidianItemTemplate(String parse) {
        List<Part> parts = ItemParser.toParts(parse);
        for (Part p : parts) {
            if (p.getType() == PartType.MATERIAL) {
                material = ObsidianMaterial.valueOf(p.getValue());
            }
            if (p.getType() == PartType.NAME) {
                name = p.getValue();
            }
            if (p.getType() == PartType.OF) {
                String entry = p.getValue();

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

        }
        // MATERIAL[_OF_ENCHANT[_LEVEL]][_NAMED_name]
    }

    /**
     * Create a template from a material
     * 
     * @param mat The material of the object
     */
    public ObsidianItemTemplate(ObsidianMaterial mat) {
        material = mat;
    }

    /**
     * Clone a ItemStack into a template
     * 
     * @param mat The item stack to copy
     */
    public ObsidianItemTemplate(ItemStack mat) {
        material = ObsidianMaterial.valueOf(mat.getType().name());
        if (mat.getType() == Material.getMaterial("SPAWNER")) {
            material = SpawnerMaterial.getMaterial(mat);
        }
        if (mat.getType().name().contains("POTION")) {
            material = PotionMaterial.getMaterial(mat);
        }
        ItemMeta itemMeta = mat.getItemMeta();
        if (itemMeta == null)
            return;

        if (itemMeta.hasDisplayName())
            name = itemMeta.getDisplayName();
        if (itemMeta.hasLore())
            lore.addAll(itemMeta.getLore());
        if (itemMeta.hasEnchants())
            enchants.putAll(itemMeta.getEnchants());
        durability = mat.getDurability();
        try {
            unbreakable = itemMeta.isUnbreakable();
        } catch (Throwable t) {

        }

        meta = itemMeta.clone();
    }

    /**
     * Copy constructor
     * 
     * @param template the template to make a copy of
     */
    public ObsidianItemTemplate(ObsidianItemTemplate template) {
        material = template.material;
        lore.addAll(template.lore);
        enchants.putAll(template.enchants);
        name = template.name;
        unbreakable = template.unbreakable;
        durability = template.durability;
        if (template.meta != null)
            meta = template.meta.clone();
    }

    /**
     * Modify the lore
     * 
     * @param lore The lines of lore
     * @return Returns a copy of the template with the modified value
     */
    public ObsidianItemTemplate lore(Collection<String> lore) {
        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        ws.lore.clear();
        if (lore.size() > 0) {
            ws.lore.addAll(lore);
        }
        return ws;
    }

    /**
     * Modify the enchants
     * 
     * @param enchants The required enchants
     * @return Returns a copy of the template with the modified value
     */
    public ObsidianItemTemplate enchants(Map<Enchantment, Integer> enchants) {
        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        ws.enchants.clear();
        if (enchants.size() > 0) {
            ws.enchants.putAll(enchants);
        }
        return ws;
    }

     /**
     * Modify the material of the template
     * 
     * @param mat New material
     * @return Returns a copy of the template with the modified value
     */
    public ObsidianItemTemplate durability(short mat) {
        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        ws.durability = mat;
        return ws;
    }


    /**
     * Modify the material of the template
     * 
     * @param mat New material
     * @return Returns a copy of the template with the modified value
     */
    public ObsidianItemTemplate material(ObsidianMaterial mat) {
        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        ws.material = mat;
        return ws;
    }

    /**
     * Modify the display name
     * 
     * @param name New display name or null to disable it
     * @return Returns a copy of the template with the modified value
     */
    public ObsidianItemTemplate name(String name) {
        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        ws.name = name;
        return ws;
    }

    /**
     * Modify the unbreakable state
     * 
     * @param unbreakable new Unbreakable value
     * @return Returns a copy of the template with the modified value
     */
    public ObsidianItemTemplate unbreakable(boolean unbreakable) {
        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        ws.unbreakable = unbreakable;
        return ws;
    }

    /**
     * Use this for advanced object that cannot be represented only with lore and
     * enchants
     * 
     * @param meta New meta information associated with the object
     * @return Returns a copy of the template with the modified value
     */
    public ObsidianItemTemplate meta(ItemMeta meta) {

        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        if (meta != null) {
            ws.meta = meta.clone();
            if (meta.hasDisplayName())
                ws.name = meta.getDisplayName();
            if (meta.hasLore()) {
                ws.lore.clear();
                ws.lore.addAll(meta.getLore());
            }
            if (meta.hasEnchants()) {
                ws.enchants.clear();
                ws.enchants.putAll(meta.getEnchants());
            }
            try {
                unbreakable = meta.isUnbreakable();
            } catch (Throwable t) {

            }
        }
        return ws;
    }

    /**
     * Get the enchants associated with the template
     * 
     * @return The full list of enchants
     */
    public Map<Enchantment, Integer> getEnchants() {
        return new HashMap<>(enchants);
    }

    /**
     * Get the lines of lore for the item template
     * 
     * @return The lines in the lore
     */
    public List<String> getLore() {
        return new ArrayList<>(lore);
    }

    /**
     * Return if the item template can be represented by the material only
     * 
     * @return Return if the item template can be represented by the material only
     */
    public boolean isPure() {
        return name == null && lore.size() == 0 && enchants.size() == 0 && meta == null && !unbreakable;
    }

    /**
     * Compare the item template with a item stack for similarity
     * 
     * @param item The item to compared againsts
     * @return If the item is similar
     */
    public boolean isSimilar(ItemStack item) {
        return toItem().isSimilar(item);
    }

    /**
     * Get the material associated with the template
     * 
     * @return The bukkit material of the object
     */
    public Material toMaterial() {
        return material.toMaterial();
    }

    /**
     * Build the material into a itemstack
     * 
     * @return A item stack
     */
    public ItemStack build() {
        return toItem();
    }

    /**
     * Build the material into a itemstack
     * 
     * @return A item stack
     */
    public ItemStack toItem() {
        ItemStack itemStack = material.toItem();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (meta != null)
            itemMeta = meta;
        if (itemMeta == null)
            return itemStack;

        if (name != null)
            itemMeta.setDisplayName(name);
        if (lore.size() > 0)
            itemMeta.setLore(lore);
        for (Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
            itemMeta.addEnchant(enchant.getKey(), enchant.getValue(), true);
        }
        try {
            itemMeta.setUnbreakable(unbreakable);
        } catch (Throwable t) {

        }
        if(durability!=0)
            itemStack.setDurability(durability);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    /**
     * Compare 2 item template for similarity
     * 
     * @param oit2 The other item template
     * @return If they are similar
     */

    public boolean isSimilar(ObsidianItemTemplate oit2) {
        return toString().equals(oit2.toString());
    }

    @Override
    public String toString() {
        return "ObsidianItemTemplate [enchants=" + enchants + ", lore=" + lore + ", material=" + material + ", meta="
                + meta + ", name=" + name + ", unbreakable=" + unbreakable + "]";
    }

}
