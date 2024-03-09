package com.moyskleytech.obsidian.material;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
public class App extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().warning("AIR = "+ObsidianMaterial.wrap(Material.AIR).normalizedName());
        getLogger().warning("minecraft:air = "+ObsidianMaterial.valueOf("minecraft:air").normalizedName());

        ObsidianMaterial.registerAllBukkitMaterials();
        ObsidianMaterial.registerPluginAdapters(this);
    }
}