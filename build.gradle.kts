buildscript {
    dependencies {
        classpath ("com.android.tools.build:gradle:8.2.2")
        // Add the Kotlin Gradle plugin (if you haven't already)
        classpath ("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.21")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.50")
    }
}

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlin) apply false
    // Hilt
    //id ("com.google.dagger.hilt.android") version "2.50" apply false
    alias (libs.plugins.hilt) apply false
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}