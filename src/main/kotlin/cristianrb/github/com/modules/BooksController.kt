package cristianrb.github.com.modules

import cristianrb.github.com.models.BookRequest
import cristianrb.github.com.models.BookResponse
import cristianrb.github.com.plugins.InvalidISBNException
import cristianrb.github.com.service.BooksService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface BooksController {
    suspend fun createBook(book: BookRequest): BookResponse
    suspend fun findBooksContainingTitle(title: String): List<BookResponse>
    suspend fun findBookById(id: Long): BookResponse
    suspend fun updateBook(book: BookRequest, id: Long): BookResponse
    suspend fun deleteBookById(id: Long)
}

class BooksControllerImpl : BooksController, KoinComponent {

    private val booksService by inject<BooksService>()

    override suspend fun createBook(book: BookRequest): BookResponse {
        if (!book.isbnIsValid()) {
            throw InvalidISBNException()
        }

        return booksService.createBook(book)
    }

    override suspend fun findBooksContainingTitle(title: String): List<BookResponse> {
        return booksService.findBooksContainingTitle(title)
    }

    override suspend fun findBookById(id: Long): BookResponse {
        return booksService.findBookById(id)
    }

    override suspend fun updateBook(book: BookRequest, id: Long): BookResponse {
        if (!book.isbnIsValid()) {
            throw InvalidISBNException()
        }

        return booksService.updateBook(book, id)
    }

    override suspend fun deleteBookById(id: Long) {
        booksService.deleteBookById(id)
    }

}