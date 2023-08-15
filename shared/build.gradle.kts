plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("com.google.devtools.ksp") version "1.9.0-1.0.12"
    id("com.rickclephas.kmp.nativecoroutines") version "1.0.0-ALPHA-15"
}

kotlin.sourceSets.all {
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCName")
    languageSettings.optIn("kotlin.experimental.ExperimentalObjCRefinement")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                //put your multiplatform dependencies here
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

android {
    namespace = "com.example.issue131"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}