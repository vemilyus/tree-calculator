import org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.detekt)

    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(libs.junitApi)
    testRuntimeOnly(libs.junitEngine)
}

kotlin {
    compilerOptions {
        jvmTarget.set(JVM_17)
    }
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("com.vemilyus.treeCalc.CalculathorKt")
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}
