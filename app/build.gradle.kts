plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(29)
    viewBinding {
        isEnabled = true
    }
    defaultConfig {
        applicationId = "com.mfriend.djapp"
        minSdkVersion(21)
        targetSdkVersion(29)
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.3.71")
    implementation("androidx.appcompat:appcompat:1.1.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

    // Timber for logging.
    implementation("com.jakewharton.timber:timber:4.7.1")

    // UI
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")
    implementation("androidx.recyclerview:recyclerview:1.1.0")

    // Ktx
    implementation("androidx.fragment:fragment-ktx:1.2.3")
    implementation("androidx.collection:collection-ktx:1.1.0")
    implementation("androidx.activity:activity-ktx:1.1.0")
    implementation("androidx.core:core-ktx:1.2.0")
    val lifeCycleVersion = "2.2.0-rc03"
    implementation("androidx.lifecycle:lifecycle-livedata-core-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifeCycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifeCycleVersion")
    val navVersion = "2.2.1"
    implementation("androidx.navigation:navigation-runtime-ktx:$navVersion")
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation("androidx.room:room-ktx:2.2.5")
    implementation("androidx.navigation:navigation-fragment-ktx:2.2.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.2.1")

    // Spotify authorization api and stuff required for it
    implementation(project(":spotify-auth"))
    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.moshi:moshi-kotlin:1.8.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    // Dont update to 1.9.2 it breaks the build :(
    //noinspection GradleDependency
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.8.0")
    implementation("androidx.navigation:navigation-fragment:2.3.0-alpha04")

    // Web request dependencies (Retrofit+MOSHI)
    implementation("com.squareup.retrofit2:retrofit:2.7.2")
    // TODO Make a build variant that adds logs
    implementation("com.squareup.okhttp3:logging-interceptor:4.2.1")
    implementation("com.squareup.retrofit2:converter-moshi:2.7.2")
    // Koin
    // Koin AndroidX Scope feature
    implementation("org.koin:koin-androidx-scope:2.0.1")
    // Koin AndroidX ViewModel feature
    implementation("org.koin:koin-androidx-viewmodel:2.0.1")

    // Room related dependencies
    // Room annotation processor
    kapt("androidx.room:room-compiler:2.2.5")
    // Room ktx and coroutines
    implementation("androidx.room:room-ktx:2.2.5")
    implementation("androidx.room:room-runtime:2.2.5")

    // Functional bb
    val arrow_version = "0.10.4"
    implementation("io.arrow-kt:arrow-core:$arrow_version")
    implementation("io.arrow-kt:arrow-syntax:$arrow_version")
    kapt("io.arrow-kt:arrow-meta:$arrow_version")

    // Coil for image loading
    implementation("io.coil-kt:coil:0.9.5")

    // Test stuff
    testImplementation("junit:junit:4.13")
    testImplementation("io.mockk:mockk:1.9.3")
    testImplementation("androidx.room:room-testing:2.2.5")
    androidTestImplementation("androidx.test.ext:junit:1.1.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

}
