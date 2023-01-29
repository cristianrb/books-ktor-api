package cristianrb.github.com.modules

import cristianrb.github.com.models.BookRequest
import cristianrb.github.com.plugins.InvalidIdException
import cristianrb.github.com.plugins.MissingArgumentException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.booksModule() {

    val booksController by inject <BooksController>()

    post("books") {
        val bookRequest = call.receive<BookRequest>()
        val insertedBook = booksController.createBook(bookRequest)
        call.respond(HttpStatusCode.Accepted, insertedBook)

    }

    get("books") {
        val title = call.request.queryParameters["title"] ?: throw MissingArgumentException("Title query parameter was not found")
        val bookResponse = booksController.findBooksContainingTitle(title)
        call.respond(HttpStatusCode.OK, bookResponse)
    }

    get("books/{id}") {
        val idStr = call.parameters["id"] ?: throw MissingArgumentException("Id path parameter was not found")
        val id: Long = stringToLong(idStr)
        val bookResponse = booksController.findBookById(id)
        call.respond(HttpStatusCode.OK, bookResponse)
    }

    put("books/{id}") {
        val bookRequest = call.receive<BookRequest>()
        val idStr = call.parameters["id"] ?: throw MissingArgumentException("Id path parameter was not found")
        val id: Long = stringToLong(idStr)

        val insertedBook = booksController.updateBook(bookRequest, id)
        call.respond(HttpStatusCode.Accepted, insertedBook)
    }

    delete("books/{id}") {
        val idStr = call.parameters["id"] ?: throw MissingArgumentException("Id path parameter was not found")
        val id: Long = stringToLong(idStr)
        booksController.deleteBookById(id)
        call.respond(HttpStatusCode.OK)
    }

}

private fun stringToLong(idStr: String): Long {
    val id: Long
    try {
        id = idStr.toLong()
    } catch (e: NumberFormatException) {
        throw InvalidIdException()
    }
    return id
}