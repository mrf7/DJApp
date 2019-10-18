import org.jetbrains.kotlin.config.KotlinCompilerVersion

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
}


android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.mfriend.djapp"
        minSdkVersion(21)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", KotlinCompilerVersion.VERSION))
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // UI
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.recyclerview:recyclerview:1.0.0")

    // Ktx
    implementation("androidx.fragment:fragment-ktx:1.1.0")
    implementation("androidx.collection:collection-ktx:1.1.0")
    // Spotify authorization api and stuff required for it
    implementation(project(":spotify-auth"))
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.github.kaaes:spotify-web-api-android:0.4.1")
    implementation("androidx.navigation:navigation-fragment:2.2.0-beta01")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.1.0")

    // Test stuff
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")
}
