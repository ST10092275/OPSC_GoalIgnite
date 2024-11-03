plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-kapt")
    base

}
base {
    archivesName.set("gradle")
    distsDirectory.set(layout.buildDirectory.dir("custom-dist"))
    libsDirectory.set(layout.buildDirectory.dir("custom-libs"))
}
android {
    namespace = "com.example.opsc7213_goalignite"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.opsc7213_goalignite"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(8) // or any compatible version
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/DEPENDENCIES"
            excludes += "/META-INF/LICENSE"
            excludes += "/META-INF/LICENSE.txt"
            excludes += "/META-INF/license.txt"
            excludes += "/META-INF/NOTICE"
            excludes += "/META-INF/NOTICE.txt"
            excludes += "/META-INF/notice.txt"
        }
    }
}

dependencies {
    // AndroidX libraries
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.circleimageview)
    implementation(libs.sdp)
    implementation(libs.recyclerview)

    // Firebase libraries
    implementation(libs.firebase.bom)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)

    // Google Play Services
    implementation(libs.play.services.auth)
    implementation(libs.play.services.drive)

    // Facebook SDK (Replace with the actual version)
    implementation(libs.facebook.login) // Example version
    implementation(libs.facebook.android.sdk) // Example version

    // Retrofit and OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Glide
    implementation("com.github.bumptech.glide:glide:4.14.2")
    kapt("com.github.bumptech.glide:compiler:4.14.2")

    // Picasso
    implementation(libs.picasso)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation (libs.kotlin.stdlib.jdk8)

    // Google API Client
    implementation(libs.google.api.client)
    implementation(libs.google.api.client.android)
    implementation(libs.google.api.client.gson)
    implementation(libs.google.api.services.drive)
    implementation(libs.google.http.client.android)
    implementation(libs.google.http.client.jackson2)

    // Guava
    implementation(libs.guava)

    // Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation ("com.squareup.dagger:dagger:1.2.5")
    kapt ("com.squareup.dagger:dagger-compiler:1.2.5")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.22")
}
