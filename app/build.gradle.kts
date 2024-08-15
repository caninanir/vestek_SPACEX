plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}



android {
    namespace = "com.caninanir.spacex"
    compileSdk = 35

    kotlinOptions {
        freeCompilerArgs += "-Xallow-kotlin-package"
    }

    defaultConfig {
        applicationId = "com.can_inanir.spacex"
        minSdk = 23
        //noinspection EditedTargetSdkVersion
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
       vectorDrawables {
            useSupportLibrary = true
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
        composeOptions {
            kotlinCompilerExtensionVersion = "1.5.3"
        }
        packaging {
            resources {
                excludes += "/META-INF/{AL2.0,LGPL2.1}"
            }
        }
        testOptions {
            unitTests.isReturnDefaultValues = true
        }
    }
    dependencies {
        implementation("androidx.core:core-ktx:1.13.1")
        implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
        implementation("androidx.activity:activity-compose:1.9.1")

        // Compose
        implementation(platform("androidx.compose:compose-bom:2024.06.00"))
        implementation("androidx.compose.ui:ui")
        implementation("androidx.compose.ui:ui-graphics")
        implementation("androidx.compose.material3:material3")
        implementation("androidx.compose.ui:ui-tooling-preview")
        debugImplementation("androidx.compose.ui:ui-tooling")
        debugImplementation("androidx.compose.ui:ui-test-manifest")

        // AndroidX Testing
        androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.8")
        androidTestImplementation("androidx.test.ext:junit:1.2.1")
        androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
        androidTestImplementation("androidx.test:core:1.6.1")
        androidTestImplementation("androidx.test:runner:1.6.2")
        androidTestImplementation("androidx.test:rules:1.6.1")

        // Navigation
        implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
        implementation("androidx.navigation:navigation-ui-ktx:2.7.7")

        // UI
        implementation("androidx.appcompat:appcompat:1.7.0")
        implementation("com.google.android.material:material:1.12.0")
        implementation("androidx.constraintlayout:constraintlayout:2.1.4")

        // Networking
        implementation("com.squareup.retrofit2:retrofit:2.9.0")
        implementation("com.squareup.retrofit2:converter-gson:2.9.0")

        // Image Loading
        implementation("io.coil-kt:coil-compose:2.4.0")

        // Firebase
        implementation(platform("com.google.firebase:firebase-bom:33.1.2"))
        implementation("com.google.firebase:firebase-auth-ktx:23.0.0")
        implementation("com.google.android.gms:play-services-auth:21.2.0")
        implementation("com.google.firebase:firebase-firestore-ktx:25.0.0")

        // Accompanist
        implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0")

        // Room
        implementation("androidx.room:room-runtime:2.6.1")
        ksp("androidx.room:room-compiler:2.6.1")
        implementation("androidx.room:room-ktx:2.6.1")

        // Hilt
        implementation("com.google.dagger:hilt-android:2.52")
        kapt("com.google.dagger:hilt-android-compiler:2.49")
        implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

        // Other
        implementation("com.jakewharton.timber:timber:5.0.1")
        implementation("com.google.code.gson:gson:2.10.1")
        implementation("androidx.appcompat:appcompat:1.7.0")

        // Unit Testing
        testImplementation("junit:junit:4.13.2")
        testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
        testImplementation("io.mockk:mockk:1.13.5")
        testImplementation("com.google.dagger:hilt-android-testing:2.52")
        kaptTest("com.google.dagger:hilt-android-compiler:2.49")
        testImplementation("androidx.arch.core:core-testing:2.2.0")

        // Android Test Implementation
        androidTestImplementation("com.google.dagger:hilt-android-testing:2.52")
        kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.49")




        //HILT
        implementation ("com.google.dagger:hilt-android:2.52")
        annotationProcessor ("com.google.dagger:hilt-compiler:2.52")

        // For instrumentation tests
        androidTestImplementation ( "com.google.dagger:hilt-android-testing:2.52")
        androidTestAnnotationProcessor ("com.google.dagger:hilt-compiler:2.52")

        // For local unit tests
        testImplementation ("com.google.dagger:hilt-android-testing:2.52")
        testAnnotationProcessor ("com.google.dagger:hilt-compiler:2.52")


        implementation ("com.google.dagger:hilt-android:2.52")
        kapt ("com.google.dagger:hilt-compiler:2.52")

        // For instrumentation tests
        androidTestImplementation  ("com.google.dagger:hilt-android-testing:2.52")
        kaptAndroidTest ("com.google.dagger:hilt-compiler:2.52")

        // For local unit tests
        testImplementation ("com.google.dagger:hilt-android-testing:2.52")
        kaptTest ("com.google.dagger:hilt-compiler:2.52")
    }
    kapt {
        correctErrorTypes = true
    }
}

