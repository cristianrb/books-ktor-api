package cristianrb.github.com.repository

import com.github.cristianrb.Tables
import cristianrb.github.com.models.BookRequest
import cristianrb.github.com.models.BookResponse
import cristianrb.github.com.plugins.BookNotFoundException
import cristianrb.github.com.plugins.JooqConfiguration
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface BooksRepository {
    suspend fun createBook(book: BookRequest): BookResponse
    suspend fun findBooksContainingTitle(title: String, limit: Int, offset: Int): List<BookResponse>
    suspend fun findBookById(id: Long): BookResponse
    suspend fun updateBook(book: BookRequest, id: Long): BookResponse
    suspend fun deleteBookById(id: Long)
}

class BooksRepositoryImpl: BooksRepository, KoinComponent {

    private val jooqConfiguration by inject<JooqConfiguration>()

    override suspend fun createBook(book: BookRequest): BookResponse {
        val booksRecord = jooqConfiguration.dslContext
            .insertInto(Tables.BOOKS, Tables.BOOKS.TITLE, Tables.BOOKS.AUTHOR, Tables.BOOKS.ISBN)
            .values(book.title, book.author, book.isbn)
            .returning()
            .fetchOne()

        return BookResponse(booksRecord!!.id, book.title, book.author, book.isbn)

    }

    override suspend fun findBooksContainingTitle(title: String, limit: Int, offset: Int): List<BookResponse> {
        val books = jooqConfiguration.dslContext
            .select(Tables.BOOKS.fields().toList())
            .from(Tables.BOOKS)
            .where(Tables.BOOKS.TITLE.containsIgnoreCase(title))
            .limit(limit)
            .offset(offset*limit)
            .fetchInto(BookResponse::class.java)

        if (books.isEmpty()) {
            throw BookNotFoundException("Book with title '$title' was not found")
        }

        return books
    }

    override suspend fun findBookById(id: Long): BookResponse {
        return jooqConfiguration.dslContext
            .select(Tables.BOOKS.fields().toList())
            .from(Tables.BOOKS)
            .where(Tables.BOOKS.ID.eq(id))
            .fetchOneInto(BookResponse::class.java) ?: throw BookNotFoundException("Book with id '$id' was not found")
    }

    override suspend fun updateBook(book: BookRequest, id: Long): BookResponse {
        val booksRecord = jooqConfiguration.dslContext
            .update(Tables.BOOKS)
            .set(Tables.BOOKS.TITLE, book.title)
            .set(Tables.BOOKS.AUTHOR, book.author)
            .set(Tables.BOOKS.ISBN, book.isbn)
            .where(Tables.BOOKS.ID.eq(id))
            .returning()
            .fetchOne() ?: throw BookNotFoundException("Book with id '$id' was not found")

        return BookResponse(booksRecord.id, book.title, book.author, book.isbn)
    }

    override suspend fun deleteBookById(id: Long) {
        jooqConfiguration.dslContext
            .delete(Tables.BOOKS)
            .where(Tables.BOOKS.ID.eq(id))
            .execute()
    }

}