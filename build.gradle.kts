plugins {
    java
    `maven-publish`
    id("dev.kikugie.stonecutter")
    id("net.neoforged.moddev") version "2.0.140" apply false
    id("fabric-loom") version "1.9-SNAPSHOT" apply false
    id("net.minecraftforge.gradle") version "[6.0.24,6.2)" apply false
    id("org.parchmentmc.librarian.forgegradle") version "1.+" apply false
    id("org.spongepowered.mixin") version "0.7-SNAPSHOT" apply false
    id("com.modrinth.minotaur") version "2.+" apply false
    id("net.darkhax.curseforgegradle") version "1.1.+" apply false
}

val modId: String by project
val modName: String by project
val modVersion: String by project
val modGroupId: String by project
val modAuthors: String by project
val modDescription: String by project
val modLicense: String by project
val minecraftVersion: String by project
val javaVersion: String by project

val loader = project.name.substringAfterLast('-')

version = modVersion
group = modGroupId
base.archivesName.set(modId)

java.toolchain.languageVersion.set(JavaLanguageVersion.of(javaVersion.toInt()))

repositories {
    maven("https://maven.parchmentmc.org/")
    maven("https://maven.shedaniel.me/")
    maven("https://maven.terraformersmc.com/")
    maven("https://maven.architectury.dev/")
    maven("https://maven.minecraftforge.net/")
}

sourceSets {
    main {
        resources.srcDir("src/generated/resources")
    }
}

// apply loader-specific build logic (Groovy scripts for dynamic typing)
when (loader) {
    "neoforge" -> apply(from = rootProject.file("neoforge.gradle"))
    "fabric" -> apply(from = rootProject.file("fabric.gradle"))
    "forge" -> apply(from = rootProject.file("forge.gradle"))
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }
    repositories {
        maven {
            url = uri("file://${project.projectDir}/repo")
        }
    }
}

// publishing to Modrinth and CurseForge
apply(plugin = "com.modrinth.minotaur")
apply(plugin = "net.darkhax.curseforgegradle")

val jarTask = when (loader) {
    "fabric" -> tasks.named("remapJar")
    else -> tasks.named("jar")
}
val loaderName = when (loader) {
    "neoforge" -> "NeoForge"
    "forge" -> "Forge"
    else -> "Fabric"
}

// extract the latest version's content from CHANGELOG.md (between first and second ## version headers)
val changelogContent = rootProject.file("CHANGELOG.md").readText()
    .substringAfter("\n## $modVersion")
    .substringAfter("\n")
    .let { block -> val next = block.indexOf("\n## "); if (next >= 0) block.substring(0, next) else block }
    .trim()

extensions.configure<com.modrinth.minotaur.ModrinthExtension> {
    token.set(findProperty("MODRINTH_TOKEN") as String?)
    projectId.set("patience")
    versionNumber.set(modVersion)
    versionName.set("$modName $modVersion ($loaderName)")
    versionType.set("release")
    uploadFile.set(jarTask)
    gameVersions.addAll(minecraftVersion)
    loaders.add(loader)
    changelog.set(changelogContent)
    dependencies {
        optional.project("cloth-config")
        optional.project("modmenu")
        optional.project("emi")
        optional.project("hexerei")
        optional.project("inventorio")
        optional.project("universal-sawmill")
        optional.project("invmove")
        optional.project("easel-does-it")
        optional.project("woodworks")
        optional.project("cold-sweat")
        optional.project("galosphere")
        optional.project("clayworks")
        optional.project("curios")
    }
}

tasks.register<net.darkhax.curseforgegradle.TaskPublishCurseForge>("curseforge") {
    apiToken = findProperty("CURSEFORGE_TOKEN") as String?
    val mainFile = upload(1401997, jarTask)
    mainFile.displayName = "$modName $modVersion ($loaderName)"
    mainFile.releaseType = "release"
    mainFile.addGameVersion(minecraftVersion)
    mainFile.addModLoader(loaderName)
    mainFile.addEnvironment("Client")
    mainFile.addEnvironment("Server")
    mainFile.changelog = changelogContent
    mainFile.changelogType = "markdown"
    mainFile.addOptional("cloth-config")
    mainFile.addOptional("modmenu")
    mainFile.addOptional("emi")
    mainFile.addOptional("hexerei")
    mainFile.addOptional("inventorio")
    mainFile.addOptional("inventorio-forge")
    mainFile.addOptional("sawmill")
    mainFile.addOptional("invmove")
    mainFile.addOptional("easel-does-it")
    mainFile.addOptional("woodworks")
    mainFile.addOptional("cold-sweat")
    mainFile.addOptional("galosphere")
    mainFile.addOptional("clayworks")
    mainFile.addOptional("curios")
}
