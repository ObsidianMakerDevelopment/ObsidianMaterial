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

import lombok.Getter;

public class ObsidianItemTemplate {

    @Getter
    private ObsidianMaterial material;

    private List<String> lore = new ArrayList<>();
    private Map<Enchantment, Integer> enchants = new HashMap<>();
    @Getter
    private String name;
    @Getter
    private boolean unbreakable;

    @Getter
    private ItemMeta meta;

    public ObsidianItemTemplate(ObsidianMaterial mat) {
        material = mat;
    }

    public ObsidianItemTemplate(ItemStack mat) {
        material = ObsidianMaterial.valueOf(XMaterial.matchXMaterial(mat).name());
        ItemMeta itemMeta = mat.getItemMeta();
        if (itemMeta == null)
            return;

        if (itemMeta.hasDisplayName())
            name = itemMeta.getDisplayName();
        if (itemMeta.hasLore())
            lore.addAll(itemMeta.getLore());
        if (itemMeta.hasEnchants()) 
            enchants.putAll(itemMeta.getEnchants());
        unbreakable = itemMeta.isUnbreakable();

        meta = itemMeta.clone();
    }

    public ObsidianItemTemplate(ObsidianItemTemplate template) {
        material = template.material;
        lore.addAll(template.lore);
        enchants.putAll(template.enchants);
        name = template.name;
        unbreakable = template.unbreakable;
        meta=template.meta;
    }

    public ObsidianItemTemplate lore(Collection<String> lore) {
        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        ws.lore.clear();
        if (lore.size() > 0) {
            ws.lore.addAll(lore);
        }
        return ws;
    }

    public ObsidianItemTemplate enchants(Map<Enchantment, Integer> enchants) {
        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        ws.enchants.clear();
        if (enchants.size() > 0) {
            ws.enchants.putAll(enchants);
        }
        return ws;
    }

    public ObsidianItemTemplate name(String name) {
        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        ws.name = name;
        return ws;
    }

    public ObsidianItemTemplate unbreakable(boolean unbreakable) {
        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        ws.unbreakable = unbreakable;
        return ws;
    }

    public Map<Enchantment,Integer> getEnchants()
    {
        return new HashMap<>(enchants);
    }
    public List<String> getLore()
    {
        return new ArrayList<>(lore);
    }

    public boolean isSimilar(ItemStack item) {
        return toItem().isSimilar(item);
    }

    public Material toMaterial() {
        return material.toMaterial();
    }
    public ItemStack build()
    {
        return toItem();
    }

    public ItemStack toItem() {
        ItemStack itemStack = material.toItem();
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(meta!=null)
            itemMeta=meta;
        if (itemMeta == null)
            return itemStack;

        if (name != null)
            itemMeta.setDisplayName(name);
        if (lore.size() > 0)
            itemMeta.setLore(lore);
        for (Entry<Enchantment, Integer> enchant : enchants.entrySet()) {
            itemMeta.addEnchant(enchant.getKey(), enchant.getValue(), true);
        }
        itemMeta.setUnbreakable(unbreakable);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
