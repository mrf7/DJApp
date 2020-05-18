buildscript {


    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(Libs.com_android_tools_build_gradle)
        classpath(Libs.kotlin_gradle_plugin)
        classpath(Libs.navigation_safe_args_gradle_plugin)
    }
}
plugins {
    id("io.gitlab.arturbosch.detekt") version "1.7.1"
    id("de.fayard.buildSrcVersions") version "0.7.0"
}

allprojects {
    apply(file("$rootDir/detekt.gradle"))
}

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