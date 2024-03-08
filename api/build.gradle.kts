plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "com.moyskleytech"
version = "1.0.8"
description = "ObsidianMaterialAPI"
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
    implementation("org.jetbrains:annotations:23.0.0")
    compileOnly("com.github.cryptomorin:XSeries:8.8.0")

    // Other dependencies that are not required or already available at runtime
    compileOnly("org.projectlombok:lombok:1.18.22")

    annotationProcessor("org.projectlombok:lombok:1.18.22")
    compileOnly("com.fasterxml.jackson.core:jackson-databind:2.13.1")
    compileOnly("com.fasterxml.jackson.core:jackson-core:2.13.1")
    compileOnly("com.github.SkriptLang:Skript:2.6.1")
    compileOnly("org.spigotmc:spigot-api:1.13-R0.1-SNAPSHOT")
    compileOnly("com.github.oraxen:oraxen:1.155.0")
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
        setGroupId("com.moyskleytech")
        setArtifactId("ObsidianMaterialAPI")
        setVersion(version)
        artifact(tasks["jar"])
    }
}

tasks.withType<Jar> {
    from(sourceSets["test"].allSource)
    {
        from(tasks["javadoc"]).into("/javadoc")
    }
}
task<Exec>("publishToM2") {
    dependsOn("publishToMavenLocal")

    commandLine("bash", "-c", "scp -r ~/.m2/repository/com/moyskleytech/ObsidianMaterialAPI/"+version+" moyskleytech.cloud:/opt/deb/m2/com/moyskleytech/ObsidianMaterialAPI")
}

tasks {
    publish{
        dependsOn("jar")
    }
    publishToMavenLocal{
        dependsOn("jar")
    }
    assemble {
        dependsOn("shadowJar")
    }

    shadowJar {
        archiveClassifier.set("")
        //relocate("com.cryptomorin.xseries", "com.moyskleytech.obsidian.material.dependencies.xseries")
        //relocate("com.fasterxml.jackson", "com.moyskleytech.obsidian.material.dependencies.fasterxml")
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