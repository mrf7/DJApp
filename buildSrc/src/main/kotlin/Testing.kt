/**
 * Class containing testing libraries
 */
object Testing {
    /**
     * http://mockk.io
     */
    const val MOCKK: String = "io.mockk:mockk:" + TestingVersions.MOCKK

    /**
     * http://junit.org
     */
    const val JUNIT: String = "org.junit.jupiter:junit-jupiter:" + TestingVersions.JUNIT

    /**
     * https://joel-costigliola.github.io/assertj/assertj-core-quick-start.html
     */
    const val ASSERT_J: String = "org.assertj:assertj-core:" + TestingVersions.ASSERT_J
}

private object TestingVersions {
    const val JUNIT: String = "5.6.2"
    const val MOCKK: String = "1.10.0"
    const val ASSERT_J: String = "3.11.1"
}