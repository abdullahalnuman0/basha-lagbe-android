// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false // serialization

    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    // On the classpath only; :app applies it when google-services.json is present.
    alias(libs.plugins.google.services) apply false

}