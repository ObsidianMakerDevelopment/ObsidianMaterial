package com.moyskleytech.obsidian.material;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.moyskleytech.obsidian.material.ItemParser.Part;
import com.moyskleytech.obsidian.material.implementations.PotionMaterial;
import com.moyskleytech.obsidian.material.implementations.SpawnerMaterial;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MaterialTests {

    private ServerMock server;
    private MockPlugin plugin;

    @BeforeEach
    public void setup() {
        server = MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin();

        ObsidianMaterial.registerAllBukkitMaterials();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void baseMaterial() {
        assert (ObsidianMaterial.valueOf("AIR").toMaterial() == Material.AIR);
        assert (ObsidianMaterial.valueOf("STONE").toMaterial() == Material.STONE);
        assert (ObsidianMaterial.valueOf("ZOMBIE_HEAD").toMaterial() == Material.ZOMBIE_HEAD);
        assert (ObsidianMaterial.valueOf("LADDER").toMaterial() == Material.LADDER);
        assert (ObsidianMaterial.valueOf("RABBIT").toMaterial() == Material.RABBIT);
        assert (ObsidianMaterial.valueOf("COW_SPAWN_EGG").toMaterial() == Material.COW_SPAWN_EGG);
    }
    @Test
    public void namespacedMaterial() {
        assert (ObsidianMaterial.valueOf("minecraft:stone").toMaterial() == Material.STONE);
    }

    @Test
    public void normalizedMaterial() {
        assertEquals ("minecraft:air",ObsidianMaterial.valueOf("AIR").normalizedName() );
        assertEquals ("minecraft:stone",ObsidianMaterial.valueOf("minecraft:stone").normalizedName());
        assertEquals ("minecraft:stone",ObsidianMaterial.valueOf("STONE").normalizedName() );
        assertEquals ("minecraft:zombie_head",ObsidianMaterial.valueOf("ZOMBIE_HEAD").normalizedName() );
        assertEquals ("minecraft:ladder",ObsidianMaterial.valueOf("LADDER").normalizedName() );
        assertEquals ("minecraft:rabbit",ObsidianMaterial.valueOf("RABBIT").normalizedName() );
        assertEquals ("minecraft:cow_spawn_egg",ObsidianMaterial.valueOf("COW_SPAWN_EGG").normalizedName());
    }

    @Test
    public void normalizedMaterial2() {
        assertEquals ("minecraft:stone",ObsidianMaterial.match(ObsidianMaterial.valueOf("STONE").toItem()).normalizedName() );
        assertEquals ("minecraft:ladder",ObsidianMaterial.match(ObsidianMaterial.valueOf("LADDER").toItem()).normalizedName() );
        assertEquals ("minecraft:rabbit",ObsidianMaterial.match(ObsidianMaterial.valueOf("RABBIT").toItem()).normalizedName() );
        assertEquals ("minecraft:cow_spawn_egg",ObsidianMaterial.match(ObsidianMaterial.valueOf("COW_SPAWN_EGG").toItem()).normalizedName());
    }

    @Test
    public void spawner() {
        ItemStack item = ObsidianMaterial.valueOf("COW_SPAWNER").toItem();
        ItemMeta itemMeta = item.getItemMeta();

        assert(itemMeta!=null);
        //assertEquals(BlockStateMeta.class,itemMeta.getClass());
    }

    @Test
    public void spawners() {
        // assertEquals("spawner:cow",
        //         SpawnerMaterial.getMaterial(ObsidianMaterial.valueOf("COW_SPAWNER").toItem()).normalizedName());
        // assertEquals("spawner:pig",
        //         SpawnerMaterial.getMaterial(ObsidianMaterial.valueOf("PIG_SPAWNER").toItem()).normalizedName());
        // assertEquals("spawner:ocelot",
        //         SpawnerMaterial.getMaterial(ObsidianMaterial.valueOf("OCELOT_SPAWNER").toItem()).normalizedName());
    }

    @Test
    public void head() {
        assertEquals("PLAYER_HEAD",
                (ObsidianMaterial.valueOf("PLAYER_HEAD").toItem().getType()).name());
        assertEquals("PLAYER_HEAD",
                (ObsidianMaterial.valueOf("Obsidian_HEAD").toItem().getType()).name());
        assertEquals("PLAYER_HEAD",
                (ObsidianMaterial.valueOf("takstijn_HEAD").toItem().getType()).name());
    }

    @Test
    public void potions() {
        // Mock bukkit doesn't implement ItemMeta Correctly cannot test those
        assertEquals(PotionType.AWKWARD + "_POTION", PotionMaterial
                .getMaterial(ObsidianMaterial.valueOf(PotionType.AWKWARD + "_POTION").toItem()).name());
        assertEquals(PotionType.FIRE_RESISTANCE + "_POTION", PotionMaterial
                .getMaterial(ObsidianMaterial.valueOf(PotionType.FIRE_RESISTANCE + "_POTION").toItem()).name());
        assertEquals(PotionType.NIGHT_VISION + "_SPLASH_POTION",
                PotionMaterial
                        .getMaterial(
                                ObsidianMaterial.valueOf(PotionType.NIGHT_VISION + "_SPLASH_POTION").toItem())
                        .name());

    }

    @Test
    public void explode()
    {
        List<Part> res = ItemParser.toParts("DIAMOND_SWORD_OF_MENDING_5_NAMED_Destroyer \\of universe");
        System.err.println(res.toString());
        assertEquals(3, res.size());
        assertEquals("DIAMOND_SWORD", res.get(0).getValue());
        assertEquals("MENDING_5", res.get(1).getValue());
        assertEquals("Destroyer of universe", res.get(2).getValue());
    }

    @Test
    public void legacyItemParser()
    {
        ObsidianItemTemplate oit = new ObsidianItemTemplate("DIAMOND_SWORD_OF_MENDING_5_NAMED_Destroyer \\of universe");
        
        ObsidianItemTemplate oit2 = new ObsidianItemTemplate(ObsidianMaterial.valueOf("DIAMOND_SWORD"))
        .name("Destroyer of universe")
        .enchants(Map.of(Enchantment.MENDING,5));

        assertEquals(oit2.toString(),oit.toString());
        assert(oit.isSimilar(oit2));
        
    }
    @Test
    public void legacyItemParser2()
    {
        ObsidianItemTemplate oit = new ObsidianItemTemplate("DIAMOND_SWORD of MENDING_5 named Destroyer \\of universe");
        
        ObsidianItemTemplate oit2 = new ObsidianItemTemplate(ObsidianMaterial.valueOf("DIAMOND_SWORD"))
        .name("Destroyer of universe")
        .enchants(Map.of(Enchantment.MENDING,5));

        assertEquals(oit2.toString(),oit.toString());
        assert(oit.isSimilar(oit2));
        
    }
    @Test
    public void items() {
        ObsidianItemTemplate template = new ObsidianItemTemplate(ObsidianMaterial.valueOf("STONE")).name("The rock");
        assert (template.toItem().isSimilar(template.toItem()));

        ObsidianItemTemplate templateCopy = new ObsidianItemTemplate(template.toItem());
        assert (template.toItem().isSimilar(templateCopy.toItem()));
    }
}
