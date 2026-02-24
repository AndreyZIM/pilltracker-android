plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.jetbrains.kotlin.serialization)
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
    api(project(":feature:aids:api"))
    implementation(project(":core:ui"))
    implementation(project(":core:common"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.jetbrains.kotlinx.serialization.json)

    implementation(libs.jetbrains.kotlinx.coroutines.android)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.google.dagger.hilt)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
}