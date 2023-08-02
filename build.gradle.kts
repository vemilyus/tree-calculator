import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17

plugins {
    kotlin("jvm") version "1.9.0"

    application
}

repositories {
    mavenCentral()
}

kotlin {
    compilerOptions {
        jvmTarget.set(JVM_17)
    }
}

application {
    mainClass.set("com.vemilyus.treeCalc.CalculathorKt")
}
