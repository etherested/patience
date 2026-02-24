pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.neoforged.net/releases")
        maven("https://maven.architectury.dev/")
        maven("https://maven.minecraftforge.net/")
        maven("https://maven.parchmentmc.org/")
        maven("https://repo.spongepowered.org/repository/maven-public/")
        maven("https://maven.kikugie.dev/snapshots")
        maven("https://maven.modrinth.com/maven")
    }
}

plugins {
    id("dev.kikugie.stonecutter") version "0.7.10"
}

stonecutter {
    centralScript = "build.gradle.kts"
    create(rootProject) {
        vers("1.20.1-fabric", "1.20.1")
        vers("1.20.1-forge", "1.20.1")
        vers("1.21.1-fabric", "1.21.1")
        vers("1.21.1-neoforge", "1.21.1")
        vcsVersion = "1.21.1-neoforge"
    }
}
