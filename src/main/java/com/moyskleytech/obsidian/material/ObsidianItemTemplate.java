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
import com.moyskleytech.obsidian.material.implementations.SpawnerMaterial;
import com.moyskleytech.obsidian.material.parsers.ObsidianItemTemplateDeserialize;
import com.moyskleytech.obsidian.material.parsers.ObsidianItemTemplateSerialize;

import lombok.Getter;

@JsonSerialize(using = ObsidianItemTemplateSerialize.class)
@JsonDeserialize(using = ObsidianItemTemplateDeserialize.class)
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

    public ObsidianItemTemplate() {
        material = ObsidianMaterial.valueOf("STONE");
    }

    public ObsidianItemTemplate(ObsidianMaterial mat) {
        material = mat;
    }

    public ObsidianItemTemplate(ItemStack mat) {
        material = ObsidianMaterial.valueOf(mat.getType().name());
        if(mat.getType() == Material.getMaterial("SPAWNER"))
        {
            material = SpawnerMaterial.getMaterial(mat);
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
        unbreakable = itemMeta.isUnbreakable();

        meta = itemMeta.clone();
    }

    public ObsidianItemTemplate(ObsidianItemTemplate template) {
        material = template.material;
        lore.addAll(template.lore);
        enchants.putAll(template.enchants);
        name = template.name;
        unbreakable = template.unbreakable;
        if (template.meta != null)
            meta = template.meta.clone();
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

    public ObsidianItemTemplate material(ObsidianMaterial mat) {
        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        ws.material = mat;
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

    public ObsidianItemTemplate meta(ItemMeta meta) {

        ObsidianItemTemplate ws = new ObsidianItemTemplate(this);
        if (meta != null)
        {
            ws.meta = meta.clone();
            if (meta.hasDisplayName())
                ws.name = meta.getDisplayName();
            if (meta.hasLore())
            {
                ws.lore.clear();
                ws.lore.addAll(meta.getLore());
            }
            if (meta.hasEnchants())
            {
                ws.enchants.clear();
                ws.enchants.putAll(meta.getEnchants());
            }
            unbreakable = meta.isUnbreakable();
        }
        return ws;
    }

    public Map<Enchantment, Integer> getEnchants() {
        return new HashMap<>(enchants);
    }

    public List<String> getLore() {
        return new ArrayList<>(lore);
    }

    public boolean isPure() {
        return name == null && lore.size() == 0 && enchants.size() == 0 && meta == null && !unbreakable;
    }

    public boolean isSimilar(ItemStack item) {
        return toItem().isSimilar(item);
    }

    public Material toMaterial() {
        return material.toMaterial();
    }

    public ItemStack build() {
        return toItem();
    }

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
        itemMeta.setUnbreakable(unbreakable);

        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
