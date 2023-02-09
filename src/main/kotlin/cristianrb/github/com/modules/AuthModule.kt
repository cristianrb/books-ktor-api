package cristianrb.github.com.modules

import cristianrb.github.com.models.User
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.authModule() {

    val authController by inject<AuthController>()

    post("login") {
        val user = call.receive<User>()
        val token = authController.generateToken(user)
        call.respond(hashMapOf("token" to token))
    }
}