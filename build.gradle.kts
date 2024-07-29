// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.5.1" apply false
    //noinspection GradleDependency
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false
    id("com.google.gms.google-services") version "4.4.2" apply false
    id("com.google.devtools.ksp") version "2.0.0-1.0.23" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.0.0" apply false


}



buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.2")
        classpath("org.jetbrains.kotlin:compose-compiler-gradle-plugin:2.0.20-Beta2")
    }
}

apply(plugin = "org.jetbrains.kotlin.plugin.compose")