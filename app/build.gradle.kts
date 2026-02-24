plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.jetbrains.kotlin.compose)
    alias(libs.plugins.google.dagger.hilt)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    id("kotlin-kapt")
}

android {
    namespace = "com.bergenproduction.pilltracker"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.bergenproduction.pilltracker"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    implementation(project(":core:ui"))
    implementation(project(":core:common"))
    implementation(project(":feature:reminders:api"))
    implementation(project(":feature:aids:impl"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.google.dagger.hilt)
    kapt(libs.google.dagger.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.google.accompanist.permissions)

    implementation(libs.jetbrains.kotlinx.serialization.json)

    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
}