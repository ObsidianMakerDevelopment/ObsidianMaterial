package com.moyskleytech.obsidian.material;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Material;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.moyskleytech.obsidian.material.implementations.SpawnerMaterial;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.MockPlugin;
import be.seeseemelk.mockbukkit.ServerMock;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MaterialTests {

    private ServerMock server;
    private MockPlugin plugin;

    @BeforeEach
    public void setup() {
        server = MockBukkit.mock();
        plugin = MockBukkit.createMockPlugin();
    }

    @AfterEach
    public void tearDown() {
        MockBukkit.unmock();
    }

    @Test
    public void baseMaterial() {
        assert(ObsidianMaterial.valueOf("AIR").toMaterial() == Material.AIR);
        assert(ObsidianMaterial.valueOf("STONE").toMaterial() == Material.STONE);
        assert(ObsidianMaterial.valueOf("ZOMBIE_HEAD").toMaterial() == Material.ZOMBIE_HEAD);
        assert(ObsidianMaterial.valueOf("LADDER").toMaterial() == Material.LADDER);
        assert(ObsidianMaterial.valueOf("RABBIT").toMaterial() == Material.RABBIT);
        assert(ObsidianMaterial.valueOf("COW_SPAWN_EGG").toMaterial() == Material.COW_SPAWN_EGG);
    }

    @Test
    public void spawners() {
       assertEquals(SpawnerMaterial.getMaterial(ObsidianMaterial.valueOf("COW_SPAWNER").toItem()).name(), "COW_SPAWNER");
       assertEquals(SpawnerMaterial.getMaterial(ObsidianMaterial.valueOf("PIG_SPAWNER").toItem()).name(), "PIG_SPAWNER");
       assertEquals(SpawnerMaterial.getMaterial(ObsidianMaterial.valueOf("OCELOT_SPAWNER").toItem()).name(), "OCELOT_SPAWNER");
    }

    @Test
    public void items()
    {
        ObsidianItemTemplate template = new ObsidianItemTemplate(ObsidianMaterial.valueOf("STONE")).name("The rock");
        assert(template.toItem().isSimilar(template.toItem()));

        ObsidianItemTemplate templateCopy = new ObsidianItemTemplate(template.toItem());
        assert(template.toItem().isSimilar(templateCopy.toItem()));
    }
}
