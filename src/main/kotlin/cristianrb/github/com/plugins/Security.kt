package cristianrb.github.com.plugins

import io.ktor.server.application.*

fun Application.configureSecurity() {
    
//    authentication {
//            jwt {
//                val jwtAudience = this@configureSecurity.environment.config.property("jwt.audience").getString()
//                realm = this@configureSecurity.environment.config.property("jwt.realm").getString()
//                verifier(
//                    JWT
//                        .require(Algorithm.HMAC256("secret"))
//                        .withAudience(jwtAudience)
//                        .withIssuer(this@configureSecurity.environment.config.property("jwt.domain").getString())
//                        .build()
//                )
//                validate { credential ->
//                    if (credential.payload.audience.contains(jwtAudience)) JWTPrincipal(credential.payload) else null
//                }
//            }
//        }
}
