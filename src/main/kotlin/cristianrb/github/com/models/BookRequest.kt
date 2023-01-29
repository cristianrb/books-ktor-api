package cristianrb.github.com.models

import kotlinx.serialization.Serializable
import java.util.regex.Pattern

@Serializable
data class BookRequest(
    val title: String,
    val author: String,
    val isbn: String
) {
    fun isbnIsValid() : Boolean {
        val regex = "^(?=(?:[^0-9]*[0-9]){10}(?:(?:[^0-9]*[0-9]){3})?$)[\\d-]+$"
        val p = Pattern.compile(regex)

        return p.matcher(isbn).matches()
    }
}
