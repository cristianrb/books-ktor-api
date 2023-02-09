package cristianrb.github.com.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

const val AUDIENCE = "AUDIENCE"
const val ISSUER = "ISSUER"
const val SECRET = "SECRET"

fun generateValidToken(): String = JWT.create()
    .withAudience(AUDIENCE)
    .withIssuer(ISSUER)
    .withClaim("username", "user")
    .withClaim("roles", listOf("CREATE_BOOKS", "READ_BOOKS"))
    .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
    .sign(Algorithm.HMAC256(SECRET))