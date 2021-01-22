import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val kotlin_version: String by project

plugins {
    kotlin("jvm") version "1.4.20"
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

group = "fi.mckits.credits"
version = "0.1.0-SNAPSHOT"

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        url = uri("http://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        url = uri("http://repo.citizensnpcs.co/")
    }
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    testImplementation(kotlin("test-junit"))
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")
    // Comment out the following line if you don't want to add the plugin in libs/ directory
    compileOnly("ga.strikepractice:strikepractice-plugin")
    // and uncomment this one
    // compileOnly("ga.strikepractice:strikepractice-api-1.1.0")
    compileOnly("me.clip:placeholderapi:2.10.4")
    compileOnly("net.citizensnpcs:citizens:2.0.13-SNAPSHOT")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("McKitsCredits")
        mergeServiceFiles()
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}