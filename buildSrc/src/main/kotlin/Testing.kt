object Testing {
    /**
     * http://mockk.io
     */
    const val MOCKK: String = "io.mockk:mockk:" + TestingVersions.MOCKK

    /**
     * https://developer.android.com/topic/libraries/architecture/index.html
     */
    const val ROOM_TESTING: String = "androidx.room:room-testing:" + AndroidXVersions.ANDROIDX_ROOM

    /**
     * http://junit.org
     */
    const val JUNIT: String = "junit:junit:" + TestingVersions.JUNIT
}

object TestingVersions {
    const val JUNIT: String = "4.13"
    const val MOCKK: String = "1.10.0"
}