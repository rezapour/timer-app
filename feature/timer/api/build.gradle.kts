plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlinSerialization)
}

tasks.matching { it.name.contains("AndroidTest") }.configureEach {
    enabled = false
}

android {
    namespace = "me.rezapour.api"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        unitTests.all {
            enableKotlin = false
        }
    }

}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.androidx.navigation3.runtime)
}