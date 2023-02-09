package cristianrb.github.com.routing

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import cristianrb.github.com.models.BookRequest
import cristianrb.github.com.models.BookResponse
import cristianrb.github.com.modules.BooksController
import cristianrb.github.com.plugins.*
import cristianrb.github.com.util.AUDIENCE
import cristianrb.github.com.util.ISSUER
import cristianrb.github.com.util.SECRET
import cristianrb.github.com.util.generateValidToken
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.response.*
import io.ktor.server.testing.*
import io.ktor.test.dispatcher.*
import io.mockk.coEvery
import io.mockk.mockk
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BooksModuleTest {

    private val booksController: BooksController = mockk()
    private lateinit var testApp: TestApplication

    @BeforeTest
    fun setup() {
        testApp = TestApplication {
            environment {
                config = MapApplicationConfig("ktor.environment" to "test")
            }
            application {
                install(Koin) {
                    modules(
                        module {
                            single { booksController }
                        }
                    )
                }
                authentication {
                    jwt {
                        verifier(
                            JWT
                                .require(Algorithm.HMAC256(SECRET))
                                .withAudience(AUDIENCE)
                                .withIssuer(ISSUER)
                                .build()
                        )
                        validate { credential ->
                            if (credential.payload.audience.contains(AUDIENCE)) JWTPrincipal(credential.payload) else null
                        }
                    }
                }
                configureRouting()
                configureSerialization()
            }
        }
    }

    @AfterTest
    fun teardown() {
        testApp.stop()
    }

    private fun TestApplication.httpClient() = createClient {
        install(ContentNegotiation) {
            json()
        }
    }

    @Test
    fun `Given a valid post book request, then we should get a book response`() = testSuspend {
        val book = BookRequest("title", "author", "978-3-16-148410-0")
        val expectedBook = BookResponse(1, "title", "author", "978-3-16-148410-0")

        coEvery { booksController.createBook(book) } returns expectedBook

        val response = testApp.httpClient().post("/books") {
            contentType(ContentType.Application.Json)
            headers["Authorization"] = "Bearer ${generateValidToken()}"
            setBody(book)
        }

        assertEquals(expectedBook, response.body())
    }

}