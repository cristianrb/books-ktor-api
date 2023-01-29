package cristianrb.github.com.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
    
    authentication {
        jwt {
            val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
            val secret = this@configureSecurity.environment.config.property("jwt.secret").getString()
            realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withAudience(jwtAudience)
                    .withIssuer(this@configureSecurity.environment.config.property("jwt.issuer").getString())
                    .build()
            )
            validate { credential ->
                if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, ApiErrorResponse(
                    error = ErrorBody(
                        message = "Invalid token provided",
                        status = "Unauthorized"
                    )
                ))
            }
        }
    }
}
