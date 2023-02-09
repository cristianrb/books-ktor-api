package cristianrb.github.com.service

import cristianrb.github.com.models.BookRequest
import cristianrb.github.com.models.BookResponse
import cristianrb.github.com.repository.BooksRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class BooksServiceTest {

    private val booksRepository: BooksRepository = mockk()
    private val service: BooksService by lazy { BooksServiceImpl() }

    @BeforeTest
    fun before() {
        GlobalContext.startKoin {
            modules(
                module {
                    single { booksRepository }
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

        coEvery { booksRepository.createBook(book) } returns expectedBook

        runBlocking {
            val actualBook = service.createBook(book)
            assertEquals(expectedBook, actualBook)
        }

    }
    
}