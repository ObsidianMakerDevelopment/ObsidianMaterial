plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.moyskleytech"
version = "1.0.0"
description = "ObsidianMaterial"
java.sourceCompatibility = JavaVersion.VERSION_1_8

repositories {
    maven("https://repo.codemc.org/repository/maven-public/")
    maven("https://repo.rosewooddev.io/repository/public/")
    maven("https://papermc.io/repo/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    mavenCentral()
}

dependencies {
    // Dependencies that we want to shade in
    implementation("org.jetbrains:annotations:23.0.0")
    implementation("com.github.cryptomorin:XSeries:8.8.0")

    // Other dependencies that are not required or already available at runtime
    compileOnly("org.projectlombok:lombok:1.18.22")

    // Enable lombok annotation processing
    annotationProcessor("org.projectlombok:lombok:1.18.22")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.13.1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.13.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.1")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.13.1")
    implementation("org.yaml:snakeyaml:1.29")
    implementation("io.papermc:paperlib:1.0.7")

    // Other dependencies that are not required or already available at runtime
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks {
    jar {
        dependsOn("shadowJar")
        enabled = false
    }

    shadowJar {
        archiveClassifier.set("")
        relocate("org.yaml.snakeyaml", "com.moyskleytech.obsidian.material.dependencies.snakeyaml")
        relocate("com.cryptomorin.xseries", "com.moyskleytech.obsidian.material.dependencies.xseries")
        relocate("com.fasterxml.jackson", "com.moyskleytech.obsidian.material.dependencies.fasterxml")
    }

    compileJava {
        options.encoding = "UTF-8"
    }
}