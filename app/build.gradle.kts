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
    implementation(Libs.kotlin_stdlib_jdk7)
    implementation(Libs.appcompat)
    implementation(Libs.legacy_support_v4)
    // Timber for logging.
    implementation(Libs.timber)

    // UI
    implementation(Libs.constraintlayout)
    implementation(Libs.recyclerview)

    // Ktx
    implementation(Libs.fragment_ktx)
    implementation(Libs.collection_ktx)
    implementation(Libs.activity_ktx)
    implementation(Libs.core_ktx)
    val lifeCycleVersion = "2.2.0-rc03"
    implementation(Libs.lifecycle_livedata_core_ktx)
    implementation(Libs.lifecycle_livedata_ktx)
    implementation(Libs.lifecycle_runtime_ktx)
    implementation(Libs.lifecycle_viewmodel_ktx)
    val navVersion = "2.2.1"
    implementation(Libs.navigation_runtime_ktx)
    implementation(Libs.navigation_fragment_ktx)
    implementation(Libs.navigation_ui_ktx)


    // Spotify authorization api and stuff required for it
    implementation(project(":spotify-auth"))

    implementation(Libs.gson)
    implementation(Libs.moshi_kotlin)
    implementation(Libs.lifecycle_extensions)
    // Dont update to 1.9.2 it breaks the build :(
    //noinspection GradleDependency
    kapt(Libs.moshi_kotlin_codegen)
    implementation(Libs.navigation_fragment)

    // Web request dependencies (Retrofit+MOSHI)
    implementation(Libs.retrofit)
    // TODO Make a build variant that adds logs
    implementation(Libs.logging_interceptor)
    implementation(Libs.converter_moshi)
    // Koin
    // Koin AndroidX Scope feature
    implementation(Libs.koin_androidx_scope)
    // Koin AndroidX ViewModel feature
    implementation(Libs.koin_androidx_viewmodel)

    // Room related dependencies
    // Room annotation processor
    kapt(Libs.room_compiler)
    // Room ktx and coroutines
    implementation(Libs.room_ktx)
    implementation(Libs.room_runtime)
    implementation(Libs.room_ktx)

    // Functional bb
    val arrow_version = "0.10.4"
    implementation(Libs.arrow_core)
    implementation(Libs.arrow_syntax)
    kapt(Libs.arrow_meta)

    // Coil for image loading
    implementation(Libs.coil)

    // Test stuff
    testImplementation(Libs.junit_junit)
    testImplementation(Libs.mockk)
    testImplementation(Libs.room_testing)
    androidTestImplementation(Libs.androidx_test_ext_junit)
    androidTestImplementation(Libs.espresso_core)
}
