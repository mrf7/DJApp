buildscript {


    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.6.3")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.71")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.2.1")
    }
}
plugins {
    id("io.gitlab.arturbosch.detekt").version("1.7.1")
}

//allprojects {
////    apply from: "$rootDir/detekt.gradle"
//}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://dl.bintray.com/arrow-kt/arrow-kt/") }
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}