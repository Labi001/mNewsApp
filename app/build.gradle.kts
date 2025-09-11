

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.example.mnewsapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.mnewsapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.example.mnewsapp.endToEnd.HiltTestRunner"

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

    kotlin {
        jvmToolchain(17)
    }

    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/LICENSE.md",
                "META-INF/LICENSE-notice.md",
                "META-INF/AL2.0",
                "META-INF/LGPL2.1"
            )
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
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.compose.material3)

    //Test
    testImplementation(libs.androidx.test.truth)
    androidTestImplementation(libs.androidx.test.truth)

    testImplementation(libs.mock.webserver)
    androidTestImplementation(libs.mock.webserver)

    testImplementation(libs.mockk.core)
    androidTestImplementation(libs.mockk.core)


    //Splash Screen
    implementation(libs.androidx.core.splashscreen)

    //Font
    implementation(libs.androidx.font.compose)

    //Serialization
    implementation(libs.kotlinx.serialization.json)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    //Font
    implementation(libs.androidx.font.compose)

    //Coil
    implementation(libs.coil.compose)

    //Windows Size Class
    implementation(libs.androidx.material3.windowSizeClass)

    //DataStore
    implementation(libs.androidx.datastore.pref)

    //Animation
    implementation(libs.ui.ui.compose)
    implementation(libs.animation.compose)

    //Compose Lifecycle
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.lifecycle.runtime)


    //Dagger-Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
  //  kapt(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    androidTestImplementation(libs.hilt.android.test)
    kaptAndroidTest(libs.hilt.android.compiler)


    //Retrofit
    implementation(libs.squareup.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.okhttp)
    implementation(libs.kotlin.serialization.converter)

    //Room
    implementation(libs.room.runtime)
    ksp(libs.room.compile)
    implementation(libs.room.ktx)
    implementation(libs.room.pagging)
    testImplementation(libs.androidx.runner)
    testImplementation(libs.androidx.rules)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    testImplementation(kotlin("test"))
}


