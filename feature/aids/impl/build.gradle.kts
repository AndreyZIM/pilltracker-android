plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.dagger.hilt)
    id("kotlin-kapt")
}

android {
    namespace = "com.bergenproduction.aids.impl"
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

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(project(":feature:reminders:api"))
    implementation(project(":feature:aids:api"))
    implementation(project(":core:ui"))

    implementation(libs.androidx.core.ktx)
//    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    implementation(libs.jetbrains.kotlinx.coroutines.android)

    implementation(libs.google.dagger.hilt)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
}