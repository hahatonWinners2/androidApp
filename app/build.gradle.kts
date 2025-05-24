plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

//val props = Properties().apply {
//    load(rootProject.file("local.properties").inputStream())
//}
//val mapsKey: String = props["MAPS_API_KEY"] as String

android {
    namespace = "com.quo.hackaton"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.quo.hackaton"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val mapsApiKey: String = project.findProperty("MAPKIT_API_KEY") as? String
            ?: throw GradleException("MAPKIT_API_KEY is not defined in gradle.properties")

        // Передаём его в манифест как плейсхолдер
        manifestPlaceholders["MAPKIT_API_KEY"] = mapsApiKey
    }

    kapt {
        correctErrorTypes = true
        arguments {
            arg("dagger.fastInit", "ENABLED")
            arg("dagger.hilt.android.internal.disableAndroidSuperclassValidation", "true")
            arg("dagger.hilt.android.internal.projectType", "APPLICATION")
            arg("dagger.hilt.internal.useAggregatingRootProcessor", "true")
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

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.maps.mobile.v4150navikit)
    implementation(libs.automotivenavigation)
    implementation(libs.roadevents)
    implementation(libs.play.services.location)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}