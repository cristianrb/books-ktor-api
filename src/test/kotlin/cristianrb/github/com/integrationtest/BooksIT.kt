package cristianrb.github.com.integrationtest

import cristianrb.github.com.models.BookRequest
import cristianrb.github.com.models.BookResponse
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import org.koin.core.context.stopKoin
import kotlin.test.*

class BooksIT {
    private lateinit var testApp: TestApplication

    @BeforeTest
    fun setup() {
        testApp = TestApplication {}
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    private fun TestApplication.httpClient() = createClient {
        install(ContentNegotiation) {
            json()
        }
    }

    @Test
    fun `Given a valid post book request, then we should get a book response`() = testSuspend {
        val book = BookRequest("title", "author", "978-3-16-148410-0")

        val loginResponse = testApp.httpClient().post("/login") {
            contentType(ContentType.Application.Json)
            setBody(mapOf(
                "username" to "testUser",
                "password" to "testPassword"
            ))
        }

        val token = loginResponse.body<Map<String, String>>()["token"]

        val response = testApp.httpClient().post("/books") {
            contentType(ContentType.Application.Json)
            headers["Authorization"] = "Bearer $token"
            setBody(book)
        }

        val actualBook = response.body<BookResponse>()

        assertNotNull(actualBook.id)
        assertEquals(book.title, actualBook.title)
        assertEquals(book.author, actualBook.author)
        assertEquals(book.isbn, actualBook.isbn)
    }
}