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
}

object GradlePluginsVersions {
    const val KOTLIN_GRADLE_PLUGIN: String = "1.3.72"
    const val NAV_SAFE_ARGS_GRADLE_PLUGIN: String = "2.2.2"
    const val ANDROID_BUILD_TOOLS_GRADLE_PLUGIN: String = "3.6.3"
}