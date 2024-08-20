package bob.e2e.presentation.dto

data class KeypadAuthResponseDto (
    val userInput: String,
    val keyHashMap: Map<String, String>
)