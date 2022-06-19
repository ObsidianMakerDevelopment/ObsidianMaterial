package com.moyskleytech.obsidian.material;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cryptomorin.xseries.XMaterial;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.moyskleytech.obsidian.material.implementations.BookMaterial;
import com.moyskleytech.obsidian.material.implementations.BukkitMaterial;
import com.moyskleytech.obsidian.material.implementations.PotionMaterial;
import com.moyskleytech.obsidian.material.implementations.SpawnerMaterial;
import com.moyskleytech.obsidian.material.parsers.*;

@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize(using = ObsidianMaterialSerialize.class)
@JsonDeserialize(using = ObsidianMaterialDeserialize.class)
public abstract class ObsidianMaterial implements Comparable<ObsidianMaterial> {
    private static Map<String, ObsidianMaterial> materials = new HashMap<>();

    @Getter
    private String key;
    public static final ObsidianMaterial remove(String s) {
        ObsidianMaterial im = materials.get(s);
        materials.remove(s);
        return im;
    }

    public static final ObsidianMaterial wrap(XMaterial mat) {
        return valueOf(mat.name());
    }

    public static final ObsidianMaterial add(ObsidianMaterial im) {
        materials.put(im.getKey(), im);
        return im;
    }
    public static final ObsidianMaterial valueOf(Material materialString) {
        return valueOf(materialString.name());
    }
    public static final ObsidianMaterial valueOf(String materialString) {
        if (materialString == null)
            return null;
        String key = materialString;
        if (materials.containsKey(key)) {
            return materials.get(key);
        }
        
        materialString = materialString.toUpperCase();

        Material mat = Material.getMaterial(materialString);
        if (mat != null) {
            materials.put(key, new BukkitMaterial(mat, key));
            return materials.get(key);
        }
        if (materialString.endsWith("_SPAWNER") && SpawnerMaterial.isSupported()) {
            String entityString = materialString.replaceAll("_SPAWNER", "");
            try {
                EntityType t = EntityType.valueOf(entityString);
                if (t != null) {
                    materials.put(key, new SpawnerMaterial(t, key));
                    return materials.get(key);
                }
            } catch (IllegalArgumentException noEntityException) {
                // Just ignore it and try parsing it with XMaterial instead
            }
        }
        if (materialString.endsWith("_POTION") && PotionMaterial.isSupported()) {
            boolean isSplash = materialString.contains("_SPLASH");
            boolean isExtented = materialString.startsWith("EXTENDED_");
            boolean isTier2 = materialString.contains("_2");
            String potionString = materialString.replaceAll("_POTION", "").replaceAll("_SPLASH", "").replaceAll("EXTENDED_", "").replaceAll("_2", "");
            try {
                PotionType t = PotionType.valueOf(potionString);
                if (t != null) {
                    materials.put(key, new PotionMaterial(t, key, isExtented, isTier2, isSplash));
                    return materials.get(key);
                }
            } catch (IllegalArgumentException noPotionException) {
                // Just ignore it and try parsing it with XMaterial instead
            }
        }
        if (materialString.endsWith("_BOOK") && BookMaterial.isSupported()) {
            //ARROW_FIRE_AND_CHANNELING_BOOK
            String bookString = materialString.replaceAll("_BOOK", "");
            Map<Enchantment,Integer> enchants = new HashMap<>();
            for(String entry:bookString.split("_AND_"))
            {
                int level=1;
                String[] elements = entry.split("_");
                boolean isNumeric = elements[elements.length - 1].matches("-?\\d+?");
                if (isNumeric) {
                    entry = entry.substring(0, entry.lastIndexOf("_"));
                    level = Integer.parseInt(elements[elements.length - 1]);
                }
                Enchantment e = Enchantment.getByName(entry);
                if(e!=null)
                {
                    enchants.put(e, level);
                }
            }
            materials.put(key, new BookMaterial(enchants, key));
            return materials.get(key);
        }
        Optional<XMaterial> xMaterial = XMaterial.matchXMaterial(materialString);
        if (xMaterial.isPresent()) {
            materials.put(key, new com.moyskleytech.obsidian.material.implementations.XMaterial(xMaterial.get(), key));
            return materials.get(key);
        }

        return null;
    }

    public static List<ObsidianMaterial> values() {
        return new ArrayList<>(materials.values());
    }

    public String name() {
        return key;
    }

    public boolean isSimilar(ItemStack item) {
        return toItem().isSimilar(item);
    }

    public static void registerKeyDeserializer(ObjectMapper objectMapper)
    {
        SimpleModule simpleModule = new SimpleModule();
        simpleModule.addKeyDeserializer(ObsidianMaterial.class, new ObsidianMaterialKeyDeserializer());
        objectMapper.registerModule(simpleModule);
    }
    public static class ObsidianMaterialKeyDeserializer extends KeyDeserializer
    {
        @Override
        public Object deserializeKey(final String key, final DeserializationContext ctxt ) throws IOException, JsonProcessingException
        {
            return valueOf(key);
        }
    }

    public abstract Material toMaterial();
    public abstract ItemStack toItem();


    @Override
    protected Object clone() throws CloneNotSupportedException {
        return this;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ObsidianMaterial))
            return false;

        return name().equals(((ObsidianMaterial) obj).name());
    }

    @Override
    public int hashCode() {
        return name().hashCode();
    }

    @Override
    public String toString() {
        return name();
    }

    @Override
    public int compareTo(ObsidianMaterial arg0) {
        return name().compareTo(((ObsidianMaterial) arg0).name());
    }
}
