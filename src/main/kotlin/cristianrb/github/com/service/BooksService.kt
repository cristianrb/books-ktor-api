package cristianrb.github.com.service

import cristianrb.github.com.models.BookRequest
import cristianrb.github.com.models.BookResponse
import cristianrb.github.com.repository.BooksRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

interface BooksService {
    suspend fun createBook(book: BookRequest): BookResponse
    suspend fun findBooksContainingTitle(title: String): List<BookResponse>
    suspend fun findBookById(id: Long): BookResponse
    suspend fun updateBook(book: BookRequest, id: Long): BookResponse
    suspend fun deleteBookById(id: Long)
}

class BooksServiceImpl: BooksService, KoinComponent {

    private val booksRepository by inject<BooksRepository>()

    override suspend fun createBook(book: BookRequest): BookResponse {
        return booksRepository.createBook(book)

    }

    override suspend fun findBooksContainingTitle(title: String): List<BookResponse> {
        return booksRepository.findBooksContainingTitle(title)
    }

    override suspend fun findBookById(id: Long): BookResponse {
        return booksRepository.findBookById(id)
    }

    override suspend fun updateBook(book: BookRequest, id: Long): BookResponse {
        return booksRepository.updateBook(book, id)
    }

    override suspend fun deleteBookById(id: Long) {
        booksRepository.deleteBookById(id)
    }

}