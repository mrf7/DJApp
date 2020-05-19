object GradlePlugins {
    /**
     * https://developer.android.com/studio
     */
    const val ANDROID_BUILD_TOOLS: String = "com.android.tools.build:gradle:" +
            GradlePluginsVersions.ANDROID_BUILD_TOOLS_GRADLE_PLUGIN

    /**
     * https://kotlinlang.org/
     */
    const val KOTLIN_GRADLE: String = "org.jetbrains.kotlin:kotlin-gradle-plugin:" +
            GradlePluginsVersions.KOTLIN_GRADLE_PLUGIN


    /**
     * https://developer.android.com/topic/libraries/architecture/index.html
     */
    const val NAV_SAFE_ARGS: String =
        "androidx.navigation:navigation-safe-args-gradle-plugin:" +
                GradlePluginsVersions.NAV_SAFE_ARGS_GRADLE_PLUGIN

    const val KTLINT: String = "org.jlleitschuh.gradle.ktlint"

    const val DETEKT: String = "io.gitlab.arturbosch.detekt"
    const val VERSIONS_TRACK: String = "com.github.ben-manes.versions"
}

object GradlePluginsVersions {
    const val KOTLIN_GRADLE_PLUGIN: String = "1.3.72"
    const val NAV_SAFE_ARGS_GRADLE_PLUGIN: String = "2.2.2"
    const val ANDROID_BUILD_TOOLS_GRADLE_PLUGIN: String = "3.6.3"
    const val KTLINT: String = "9.2.1"
    const val DETEKT: String = "1.9.1"
    const val VERSIONS_TRACK = "0.28.0"
}