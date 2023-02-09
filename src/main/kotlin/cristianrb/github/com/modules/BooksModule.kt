package cristianrb.github.com.modules

import cristianrb.github.com.models.BookRequest
import cristianrb.github.com.plugins.ApiErrorResponse
import cristianrb.github.com.plugins.ErrorBody
import cristianrb.github.com.plugins.InvalidIdException
import cristianrb.github.com.plugins.MissingArgumentException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.booksModule() {

    val booksController by inject <BooksController>()

    authenticate {
        post("books") {
            val user = call.principal<JWTPrincipal>()
            val roles = user?.payload?.getClaim("roles")?.asList(String::class.java)?: emptyList()
            if (roles.contains("CREATE_BOOKS")) {
                val bookRequest = call.receive<BookRequest>()
                val insertedBook = booksController.createBook(bookRequest)
                call.respond(HttpStatusCode.Accepted, insertedBook)
            } else {
                call.respond(HttpStatusCode.Forbidden, ApiErrorResponse(
                    error = ErrorBody(
                        message = "Permission denied",
                        status = "Forbidden"
                    )
                ))
            }

        }

        get("books") {
            val title = call.request.queryParameters["title"] ?: throw MissingArgumentException("Title query parameter was not found")
            val limit = call.request.queryParameters["limit"] ?: "10"
            val offset = call.request.queryParameters["offset"] ?: "0"
            val bookResponse = booksController.findBooksContainingTitle(title, stringToInt(limit), stringToInt(offset))
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

private fun stringToInt(idStr: String): Int {
    val num: Int
    try {
        num = idStr.toInt()
    } catch (e: NumberFormatException) {
        throw InvalidIdException()
    }
    return num
}