plugins {
    kotlin("jvm") version "1.9.0"
    id("org.jetbrains.compose") version "1.5.0"
    kotlin("plugin.serialization") version "1.9.0"
}

group = "app.majodesk"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    testImplementation(kotlin("test"))
    implementation(compose.desktop.currentOs)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
    implementation(compose.materialIconsExtended)
}

compose.desktop {
    application {
        mainClass = "app.majodesk.MainKt"

        nativeDistributions {
            packageName = "Majo desktop"
            packageVersion = "1.0.0"
            description = "(v1.0.0) Приложение majo. Персонализированный трекер/логгер саморазвития"

            linux {
                appCategory = "Utility"
            }

            targetFormats(
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.AppImage,
                org.jetbrains.compose.desktop.application.dsl.TargetFormat.Deb
            )
        }
    }
}

kotlin {
    jvmToolchain(17)
}