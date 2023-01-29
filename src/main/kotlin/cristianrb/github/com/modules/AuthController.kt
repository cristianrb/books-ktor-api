package cristianrb.github.com.modules

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import cristianrb.github.com.models.User
import org.koin.core.component.KoinComponent
import java.util.*

interface AuthController {
    suspend fun generateToken(user: User): String
}

class AuthControllerImpl(
    private val secret: String,
    private val issuer: String,
    private val audience: String
) : AuthController, KoinComponent {

    override suspend fun generateToken(user: User): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + 3600000))
            .sign(Algorithm.HMAC256(secret))
    }
}