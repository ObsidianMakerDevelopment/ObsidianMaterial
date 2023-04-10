plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.moyskleytech"
version = "1.0.4"
description = "ObsidianMaterial"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.skriptlang.org/releases")
    maven("https://jitpack.io")
    mavenCentral()
}

dependencies {
    // Dependencies that we want to shade in
    implementation(project(":ObsidianMaterialAPI"))
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("com.github.cryptomorin:XSeries:8.8.0")

    // Other dependencies that are not required or already available at runtime
    compileOnly("org.projectlombok:lombok:1.18.22")

    annotationProcessor("org.projectlombok:lombok:1.18.22")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.1")
    implementation("org.yaml:snakeyaml:1.29")
    compileOnly("com.github.SkriptLang:Skript:2.6.1")
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")
    compileOnly("com.github.oraxen:oraxen:-SNAPSHOT")
    compileOnly("com.github.LoneDev6:api-itemsadder:3.0.0")
    compileOnly("com.github.slimefun:Slimefun4:RC-27")

    testImplementation(platform("org.junit:junit-bom:5.7.0"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("junit:junit:4.12")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.16:1.0.0")
    //testImplementation("com.github.cryptomorin:XSeries:8.8.0")
    //testImplementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    //testImplementation("com.fasterxml.jackson.core:jackson-core:2.13.1")
    testImplementation("commons-lang:commons-lang:2.4")
    compileOnly("com.github.seeseemelk:MockBukkit-v1.16:1.0.0")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<Jar> {
    from(sourceSets["test"].allSource)
    {
        from(tasks["javadoc"]).into("/javadoc")
    }
}

tasks {
    assemble {
        dependsOn("shadowJar")
    }

    shadowJar {
        archiveClassifier.set("")
        relocate("com.cryptomorin.xseries", "com.moyskleytech.obsidian.material.dependencies.xseries")
        relocate("com.fasterxml.jackson", "com.moyskleytech.obsidian.material.dependencies.fasterxml")
        relocate("org.yaml.snakeyaml", "com.moyskleytech.obsitian.matetial.dependencies.snakeyaml")
        //minimize()
    }

    test {
        useJUnitPlatform()
        //java.sourceCompatibility = JavaVersion.VERSION_17
    }

    compileJava {
        options.encoding = "UTF-8"
    }
}