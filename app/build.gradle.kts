plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Sdk.COMPILE_SDK_VERSION)
    viewBinding {
        isEnabled = true
    }
    defaultConfig {
        applicationId = "com.mfriend.djapp"
        minSdkVersion(Sdk.MIN_SDK_VERSION)
        targetSdkVersion(Sdk.TARGET_SDK_VERSION)
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

    lintOptions {
        disable.add("InvalidPackage")
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    // Android support stuff i dont actually know what these do
    implementation(AndroidX.APP_COMPAT)
    implementation(AndroidX.LEGACY_SUPPORT_V4)

    // UI
    implementation(AndroidX.CONSTRAINT_LAYOUT)
    implementation(AndroidX.RECYCLERVIEW)

    // Ktx
    implementation(AndroidX.FRAGMENT_KTX)
    implementation(AndroidX.COLLECTION_KTX)
    implementation(AndroidX.ACTIVITY_KTX)
    implementation(AndroidX.CORE_KTX)
    implementation(AndroidX.LIFECYCLE_LIVEDATA_CORE_KTX)
    implementation(AndroidX.LIFECYCLE_LIVEDATA_KTX)
    implementation(AndroidX.LIFECYCLE_RUNTIME_KTX)
    implementation(AndroidX.LIFECYCLE_VIEWMODEL_KTX)
    implementation(AndroidX.NAVIGATION_RUNTIME_KTX)
    implementation(AndroidX.NAVIGATION_FRAGMENT_KTX)
    implementation(AndroidX.NAVIGATION_UI_KTX)

    // Lifecycle extensions for viewmodel and stuff
    implementation(AndroidX.LIFECYCLE_EXTENSIONS)

    // Timber for logging.
    implementation(Libs.TIMBER)

    // Spotify authorization api and stuff required for it
    implementation(project(":spotify-auth"))

    // Web request dependencies (Retrofit+MOSHI)
    //noinspection GradleDependency
    kapt(Libs.MOSHI_KOTLIN_CODEGEN)

    implementation(Libs.RETROFIT2)

    // TODO Make a build variant that adds logs
    implementation(Libs.OKHTTP_LOGGING_INTERCEPTOR)
    implementation(Libs.RETROFIT_MOSHI_CONVERTER)

    // Koin
    // Koin AndroidX Scope feature
    implementation(Libs.KOIN_ANDROIDX_SCOPE)
    // Koin AndroidX ViewModel feature
    implementation(Libs.KOIN_VIEWMODEL)

    // Room related dependencies
    // Room annotation processor
    kapt(AndroidX.ROOM_COMPILER)
    // Room ktx and coroutines
    implementation(AndroidX.ROOM_KTX)
    implementation(AndroidX.ROOM_RUNTIME)
    implementation(AndroidX.ROOM_KTX)

    // Functional bb
    implementation(Libs.ARROW_CORE)
    implementation(Libs.ARROW_SYNTAX)
    kapt(Libs.ARROW_META)

    // Coil for image loading
    implementation(Libs.COIL)

    // Test stuff
    testImplementation(Testing.JUNIT)
    testImplementation(Testing.ASSERT_J)
    testImplementation(Testing.MOCKK)
    testImplementation(AndroidTesting.ROOM_TESTING)
    androidTestImplementation(AndroidTesting.ANDROIDX_JUNIT)
    androidTestImplementation(AndroidTesting.ESPRESSO_CORE)
}