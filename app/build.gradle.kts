plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.androyal.subxplayer"
    compileSdk = 34 // original 36, using 34 for CI runner compatibility

    defaultConfig {
        applicationId = "com.androyal.subxplayer"
        minSdk = 26 // original 24, bumped for Tika + adaptive icon (requires 26)
        targetSdk = 34
        versionCode = 47
        versionName = "2.3.1"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables { useSupportLibrary = true }
        ndk { abiFilters += listOf("arm64-v8a") }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            // applicationIdSuffix removed to match google-services.json package
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }

    packaging {
        jniLibs { useLegacyPackaging = true }
        resources { excludes += "/META-INF/{AL2.0,LGPL2.1}" }
    }

    androidResources { generateLocaleConfig = false }
}

dependencies {
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.10.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.material:material-icons-extended")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.navigation:navigation-compose:2.8.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.6")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.6")

    // Material You dynamic color
    implementation("androidx.compose.material3:material3-window-size-class:1.3.1")

    // Core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")

    // ViewModel + Navigation + Lifecycle
    implementation("androidx.navigation:navigation-fragment:2.8.2")
    implementation("androidx.navigation:navigation-ui:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.50")
    ksp("com.google.dagger:hilt-android-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")

    // Network
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // Media3 ExoPlayer (replaces media_kit + mpv + ffmpeg-kit)
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-exoplayer-hls:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")
    implementation("androidx.media3:media3-session:1.4.1")
    implementation("androidx.media3:media3-common:1.4.1")
    implementation("androidx.media:media:1.7.0")

    // FFmpegKit (optional, native libs bundled separately)
    // implementation("com.github.AntonKarpenko:ffmpeg-kit:6.0-2.LTS")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.4.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-config-ktx")

    // ML Kit Translate
    implementation("com.google.mlkit:translate:17.0.3")
    implementation("com.google.mlkit:common:18.11.0")

    // Cast
    implementation("com.google.android.gms:play-services-cast-framework:21.4.0")
    implementation("com.google.android.gms:play-services-cast:21.4.0")

    // Billing + RevenueCat
    implementation("com.revenuecat.purchases:purchases:8.12.1")
    implementation("com.android.billingclient:billing:7.1.1")
    implementation("com.android.billingclient:billing-ktx:7.1.1")

    // Permissions
    // Permissions (accompanist deprecated, use activity result APIs)
    // implementation("com.google.accompanist:accompanist-permissions:0.34.0")
    implementation("com.karumi:dexter:6.2.3")

    // Image + File
    implementation("io.coil-kt:coil-compose:2.7.0")
    implementation("io.coil-kt:coil-svg:2.7.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")

    // Utils
    implementation("androidx.documentfile:documentfile:1.0.1")
    // File utils (optional)
    // implementation("me.zhanghai.android.filesystem:filesystem:1.0.0")
    implementation("org.apache.tika:tika-core:2.9.1")
    implementation("net.lingala.zip4j:zip4j:2.11.5")

    // AnkiDroid API (optional, via reflection)
    // implementation("com.ichi2.anki:api:0.2.1") { isTransitive = false }

    // TTS + Sherpa ONNX (bundled .so) - platform TTS via Android framework
    // implementation("androidx.speech:speech:1.0.0") // not a real artifact, removed

    // Lottie
    implementation("com.airbnb.android:lottie-compose:6.5.2")

    // Shimmer + etc
    implementation("com.valentinilk.shimmer:compose-shimmer:1.0.5")
    implementation("androidx.palette:palette-ktx:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

    // Paging
    implementation("androidx.paging:paging-runtime-ktx:3.3.5")
    implementation("androidx.paging:paging-compose:3.3.5")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
