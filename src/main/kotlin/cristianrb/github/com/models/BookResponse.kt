package cristianrb.github.com.models

import kotlinx.serialization.Serializable

@Serializable
data class BookResponse(
    val id: Long,
    val title: String,
    val author: String,
    val isbn: String
)