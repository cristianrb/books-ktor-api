package cristianrb.github.com.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val password: String
)