plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-parcelize")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "ie.setu.museum"
    compileSdk = 34

    defaultConfig {
        applicationId = "ie.setu.museum"
        minSdk = 30
        targetSdk = 34
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
    
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }


}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.github.ajalt:timberkt:1.5.1")
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.google.code.gson:gson:2.10")
    implementation("com.squareup.picasso:picasso:2.71828")
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation ("com.github.denzcoskun:ImageSlideshow:0.1.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.3")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("androidx.fragment:fragment-ktx:1.6.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.4")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.facebook.android:facebook-login:latest.release")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}