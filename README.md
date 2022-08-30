# ObsidianMaterial

Maven repository
```xml
<repository>
    <id>obsidianmakerdevelopment</id>
    <url>https://moyskleytech.com/debian/m2</url>
</repository>
```
```xml
<dependency>
    <groupId>com.moyskleytech</groupId>
    <artifactId>ObsidianMaterial</artifactId>
    <version>1.0.1</version>
</dependency>
```
To use in gradle
```gradle
repositories {
    maven("https://moyskleytech.com/debian/m2")
}
dependencies {
    implementation("com.moyskleytech:ObsidianMaterialAPI:1.0.1")
}
//Some features require XSeries and FasterXML, such features are marked in Javadoc, you can also depends on the ObsidianMaterial plugin
dependencies {
    implementation("com.github.cryptomorin:XSeries:8.8.0")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.1")
}
```
You can also use the plugin based with XSeries and Jackson FasterXML included, simply add ObsidianMaterial as a depends on your plugin
```gradle
dependencies {
    compileOnly("com.moyskleytech:ObsidianMaterial:1.0.1")
}
```
# Hooks
The library hooks into some Minecraft plugin if present, as such your plugin should SoftDepends on them
- Skript
- Oraxen
- ItemsAdder
- Slimefun

# Javadoc
Javadoc is available at https://obsidianmakerdevelopment.github.io/ObsidianMaterial

# Usage
To get a Material, ObsidianMaterial.valueOf()
```java
import com.moyskleytech.obsidian.material.ObsidianMaterial;
ObsidianMaterial material = ObsidianMaterial.valueOf("RED_BED");
```
To get a Item, ItemParser.deserialize()
```java
import com.moyskleytech.obsidian.material.ObsidianItemTemplate;
import com.moyskleytech.obsidian.material.ItemParser;
ObsidianItemTemplate template = ItemParser.deserialize("RED_BED");
//To clone an item
ObsidianItemTemplate template = new ObsidianItemTemplate({**ItemStack**});
```


To get a Bukkit Material
```java
ObsidianMaterial material = ...;
org.bukkit.Material mat = material.toMaterial();
//Also available with ItemTemplate
ObsitianItemTemplate template =...;
org.bukkit.Material mat = template.toMaterial();
```
To get a Bukkit ItemStack
```java
ObsidianMaterial material = ...;
org.bukkit.inventory.ItemStack stack = material.toItem();
//Also available with ItemTemplate
ObsitianItemTemplate template =...;
org.bukkit.inventory.ItemStack stack= template.toItem();
```

Syntax for Items is still a WIP, however it is expected to support both JSON format and legacy string 
```java
"DIAMOND_SWORD_OF_MENDING_5_NAMED_Destroyer \\of universe"
```
```json
{
    "name":"Destroyer of universe",
    "material":"DIAMOND_SWORD",
    "enchants":
    { 
        "MENDING":5
    }
}
```

Acceptable values:
 - All `org.bukkit.Material` values
 - {ENTITY}_SPAWNER, {ENTITY} is any `org.bukkit.EntityType`
 - {ENCHANT}_BOOK, {ENCHANT} is any `org.bukkit.enchantments.Enchantment`
 - {ENCHANT}_{LEVEL}_BOOK
 - {ENCHANT}_{LEVEL}_AND\_{ENCHANT}\_{LEVEL}_BOOK (You can add infinite _AND\_)
 - {POTION_TYPE}_POTION, {POTION_TYPE} is any `org.bukkit.potion.PotionType`
 - {POTION_TYPE}_SPLASH_POTION
 - {POTION_TYPE}_2_POTION
 - {POTION_TYPE}_2_SPLASH_POTION
 - EXTENDED_{POTION_TYPE}_POTION
 - EXTENDED_{POTION_TYPE}_SPLASH_POTION
 - EXTENDED_{POTION_TYPE}_2_POTION
 - EXTENDED_{POTION_TYPE}_2_SPLASH_POTION
 - All `com.cryptomorin.xseries.XMaterial` values
 - {PLAYER_NAME}_HEAD

 # More blocks provider?

 ObsidianMaterial support adding more items and block provider by registering a hook.

 ```java 
 ObsidianMaterial.registerAdapter(Your adapter);
 ```

 You simply to have to comply to our very easy interface
 ```java 
 package com.moyskleytech.obsidian.material.implementations.adapters;

/**
 * Adapter class for parsing, do not implement
 */
public interface Adapter {
    /**
     * Try parsing a string as material using the specified adapter
     * @param materialString The String to parse
     * @return Maybe the material
     */
    Optional<ObsidianMaterial> tryParse(String materialString);
    Optional<ObsidianMaterial> tryMatch(ItemStack stack);
}
```