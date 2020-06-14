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
    id(GradlePlugins.VERSIONS_TRACK) version GradlePluginsVersions.VERSIONS_TRACK
    id(GradlePlugins.KTLINT_PLUGIN) version GradlePluginsVersions.KTLINT_PLUGIN
    id(GradlePlugins.DETEKT) version GradlePluginsVersions.DETEKT
}

subprojects {
    apply {
        plugin(GradlePlugins.DETEKT)
        plugin(GradlePlugins.KTLINT_PLUGIN)
    }

    ktlint {
        debug.set(false)
        // Use Versions.KTLINT to get the version of ktlint instead of the version of the plugin found
        // in GradlePluginsVersions
        version.set(GradlePluginsVersions.KTLINT)
        android.set(true)
        enableExperimentalRules.set(true)
        additionalEditorconfigFile.set(file("$rootDir/config/ktlint/.editorconfig"))
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    detekt {
        config = files("$rootDir/config/detekt/detekt.yml")
        parallel = true
        autoCorrect = true
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
