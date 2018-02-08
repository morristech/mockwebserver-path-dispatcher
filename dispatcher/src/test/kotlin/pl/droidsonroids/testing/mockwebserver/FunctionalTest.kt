package pl.droidsonroids.testing.mockwebserver

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import pl.droidsonroids.testing.mockwebserver.condition.PathQueryConditionFactory
import org.assertj.core.api.Assertions.assertThat

private const val TEST_JSON = """{
  "test": null
}"""

class FunctionalTest {

    @JvmField @Rule val mockWebServer = MockWebServer()

    private var port: Int = 0
    private lateinit var client: OkHttpClient
    private lateinit var requestBuilder: Request.Builder

    @Before
    fun setUp() {
        port = mockWebServer.port
        client = OkHttpClient()
        requestBuilder = Request.Builder()
    }

    @Test
    fun `responds with correct fixtures`() {
        val dispatcher = FixtureDispatcher()

        //all URLs beginning with http://example.test/user/
        val factory = PathQueryConditionFactory("/user/")

        /*
            all URLs with path ending with events e.g.:
            http://example.test/user/events
            http://example.test/user/events?id=1
            http://example.test/user/events?own=true
         */
        dispatcher.putResponse(factory.withPathSuffix("events"), "body_path")

        /*
            all URLs with path ending with profile e.g.:
            http://example.test/user/profile
            http://example.test/user/profile?picture=true
        */
        dispatcher.putResponse(factory.withPathSuffix("profile"), "json_object")

        mockWebServer.setDispatcher(dispatcher)

        val events = "http://localhost:$port/user/events".download()
        assertThat(events).isEqualTo("fixtures/body.txt".getResourceAsString())

        val event = "http://localhost:$port/user/events?id=1".download()
        assertThat(event).isEqualTo("fixtures/body.txt".getResourceAsString())

        val ownEvents = "http://localhost:$port/user/events?own=true".download()
        assertThat(ownEvents).isEqualTo("fixtures/body.txt".getResourceAsString())

        val profile = "http://localhost:$port/user/profile".download()
        assertThat(profile).isEqualTo(TEST_JSON)

        val picture = "http://localhost:$port/user/profile?picture=true".download()
        assertThat(picture).isEqualTo(TEST_JSON)
    }

    private fun String.download() = client.newCall(
        requestBuilder.url(this)
            .get()
            .build()
    ).execute()
        .body()
        ?.string()
}