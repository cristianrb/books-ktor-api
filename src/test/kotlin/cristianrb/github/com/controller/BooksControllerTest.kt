package cristianrb.github.com.controller

import cristianrb.github.com.models.BookRequest
import cristianrb.github.com.models.BookResponse
import cristianrb.github.com.modules.BooksController
import cristianrb.github.com.modules.BooksControllerImpl
import cristianrb.github.com.service.BooksService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BooksControllerTest {

    private val booksService: BooksService = mockk()
    private val controller: BooksController by lazy { BooksControllerImpl() }

    @BeforeTest
    fun before() {
        stopKoin()
        startKoin {
            modules(
                module {
                    single { booksService }
                }
            )
        }
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
    }


    @Test
    fun `Given a valid book request, then we should get a book response with a new id`() {
        val book = BookRequest("title", "author", "978-3-16-148410-0")
        val expectedBook = BookResponse(1, "title", "author", "978-3-16-148410-0")

        coEvery { booksService.createBook(book) } returns expectedBook

        runBlocking {
            val response = controller.createBook(book)

            assertEquals(1, response.id)
            assertEquals("title", response.title)
            assertEquals("author", response.author)
            assertEquals("978-3-16-148410-0", response.isbn)
        }

    }

}