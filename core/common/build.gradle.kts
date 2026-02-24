plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.dagger.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.bergenproduction.common"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        minSdk = 29
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)

    implementation(libs.google.dagger.hilt)
    kapt(libs.google.dagger.hilt.compiler)
}