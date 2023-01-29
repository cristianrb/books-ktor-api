package cristianrb.github.com.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<Exception> { call, cause ->
            call.respond(HttpStatusCode.InternalServerError, ApiErrorResponse(
                error = ErrorBody(
                    message = cause.localizedMessage,
                    status = "Internal server error"
                )
            ))
        }

        exception<InvalidISBNException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ApiErrorResponse(
                error = ErrorBody(
                    message = cause.message,
                    status = "Bad request"
                )
            ))
        }

        exception<MissingArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ApiErrorResponse(
                error = ErrorBody(
                    message = cause.message,
                    status = "Bad request"
                )
            ))
        }

        exception<BookNotFoundException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, ApiErrorResponse(
                error = ErrorBody(
                    message = cause.message,
                    status = "Not found"
                )
            ))
        }

        exception<InvalidIdException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ApiErrorResponse(
                error = ErrorBody(
                    message = cause.message,
                    status = "Bad request"
                )
            ))
        }
    }
}

@Serializable
data class ApiErrorResponse(
    val error: ErrorBody
)

@Serializable
data class ErrorBody(
    val message: String,
    val status: String,
)

data class InvalidISBNException(override val message: String = "Invalid ISBN") : Exception()
data class InvalidIdException(override val message: String = "Invalid ID") : Exception()
data class MissingArgumentException(override val message: String = "There is a missing argument") : Exception()
data class BookNotFoundException(override val message: String = "Book not found") : Exception()