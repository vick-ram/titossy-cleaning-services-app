plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin)
    alias (libs.plugins.hilt)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.titossycleaningservicesapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.titossycleaningservicesapp"
        minSdk = 26
        //noinspection OldTargetApi
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    //hilt
    implementation (libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.coil.compose)
    implementation(libs.androidx.room.runtime)
    implementation(libs.retrofit)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.runtime.compose.android)
    implementation(libs.ui)
    implementation(libs.itextpdf)
    implementation(libs.core)
    implementation(libs.androidx.constraintlayout.compose)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    implementation(libs.symbol.processing.api)
    ksp (libs.hilt.compiler)
    ksp (libs.dagger.compiler)
    kspTest (libs.hilt.compiler)
    //navigation
    implementation(libs.navigation.compose)
    //viewModel
    implementation(libs.lifecycle.viewmodel.compose)
    //extended icons
    implementation(libs.material.icons.extended)
    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}