package cristianrb.github.com.plugins

import cristianrb.github.com.modules.authModule
import cristianrb.github.com.modules.booksModule
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        authModule()
        booksModule()
    }
}
