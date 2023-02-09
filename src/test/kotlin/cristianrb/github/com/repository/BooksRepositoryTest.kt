package cristianrb.github.com.repository

import com.github.cristianrb.Tables
import com.github.cristianrb.tables.records.BooksRecord
import cristianrb.github.com.models.BookRequest
import cristianrb.github.com.models.BookResponse
import cristianrb.github.com.plugins.JooqConfiguration
import cristianrb.github.com.service.BooksService
import cristianrb.github.com.service.BooksServiceImpl
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import kotlin.test.assertEquals
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class BooksRepositoryTest {

    private val jooqConfiguration: JooqConfiguration = mockk()
    private val repo: BooksRepository by lazy { BooksRepositoryImpl() }

    @BeforeTest
    fun before() {
        GlobalContext.startKoin {
            modules(
                module {
                    single { jooqConfiguration }
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

        coEvery { jooqConfiguration.dslContext
            .insertInto(Tables.BOOKS, Tables.BOOKS.TITLE, Tables.BOOKS.AUTHOR, Tables.BOOKS.ISBN)
            .values(book.title, book.author, book.isbn)
            .returning()
            .fetchOne()
        } returns BooksRecord(expectedBook.id, expectedBook.title, expectedBook.author, expectedBook.isbn)

        runBlocking {
            val actualBook = repo.createBook(book)
            assertEquals(expectedBook, actualBook)
        }

    }

}