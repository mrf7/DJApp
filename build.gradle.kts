import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

buildscript {


    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(GradlePlugins.ANDROID_BUILD_TOOLS)
        classpath(GradlePlugins.KOTLIN_GRADLE)
        classpath(GradlePlugins.NAV_SAFE_ARGS)
    }
}
plugins {
    id("io.gitlab.arturbosch.detekt") version "1.9.1"
    id("com.github.ben-manes.versions") version "0.28.0"
    id("org.jlleitschuh.gradle.ktlint") version "9.2.1"
}

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
        plugin("org.jlleitschuh.gradle.ktlint")
    }

    ktlint {
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    detekt {
        config = files("$rootDir/config/detekt/detekt.yml")
        parallel = true
        reports {
            html {
                enabled = true
                destination = file("build/reports/detekt.html")
            }
        }
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { url = uri("https://dl.bintray.com/arrow-kt/arrow-kt/") }
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        isNonStable(candidate.version)
    }
}

fun isNonStable(version: String) = "^[0-9,.v-]+(-r)?$".toRegex().matches(version).not()