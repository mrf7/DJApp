/**
 * Class for holding runtime dependencies
 */
object Libs {

    /**
     * https://github.com/Kotlin/kotlinx.coroutines
     */
    const val KOTLIN_COROUTINES =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core" + Versions.KOTLIN_COROUTINES

    /**
     * https://github.com/JakeWharton/timber
     */
    const val TIMBER: String = "com.jakewharton.timber:timber:" + Versions.timber

    /**
     * https://github.com/coil-kt/coil
     */
    const val COIL: String = "io.coil-kt:coil:" + Versions.COIL

    /**
     * https://square.github.io/okhttp/
     */
    const val OKHTTP_LOGGING_INTERCEPTOR: String = "com.squareup.okhttp3:logging-interceptor:" +
            Versions.OKHTTP_LOGGING_INTERCEPTOR

    /**
     * https://github.com/square/retrofit/
     */
    const val RETROFIT_MOSHI_CONVERTER: String = "com.squareup.retrofit2:converter-moshi:" +
            Versions.RETROFIT2

    /**
     * https://github.com/square/retrofit/
     */
    const val RETROFIT2: String = "com.squareup.retrofit2:retrofit:" +
            Versions.RETROFIT2

    /**
     * https://github.com/square/moshi
     */
    const val MOSHI_KOTLIN: String = "com.squareup.moshi:moshi-kotlin:" +
            Versions.MOSHI_KOTLIN

    /**
     * https://github.com/square/moshi
     */
    const val MOSHI_KOTLIN_CODEGEN: String = "com.squareup.moshi:moshi-kotlin-codegen:" +
            Versions.MOSHI_KOTLIN

    /**
     * https://github.com/arrow-kt/arrow/
     */
    const val ARROW_CORE: String = "io.arrow-kt:arrow-core:" + Versions.ARROW

    /**
     * https://github.com/arrow-kt/arrow/
     */
    const val ARROW_META: String = "io.arrow-kt:arrow-meta:" + Versions.ARROW

    /**
     * https://github.com/arrow-kt/arrow/
     */
    const val ARROW_SYNTAX: String = "io.arrow-kt:arrow-syntax:" + Versions.ARROW

    const val KOIN_ANDROIDX_SCOPE: String = "org.koin:koin-androidx-scope:" + Versions.KOIN

    /**
     *
     */
    const val KOIN_VIEWMODEL: String = "org.koin:koin-androidx-viewmodel:" +
            Versions.KOIN
}

private object Versions {
    const val KOTLIN_COROUTINES: String = "1.3.7"
    const val RETROFIT2: String = "2.9.0"
    const val MOSHI_KOTLIN: String = "1.8.0" // available: "1.9.2"
    const val ARROW: String = "0.10.5"
    const val KOIN: String = "2.1.6"
    const val OKHTTP_LOGGING_INTERCEPTOR: String = "4.7.2"
    const val timber: String = "4.7.1"
    const val COIL: String = "0.11.0"
}
