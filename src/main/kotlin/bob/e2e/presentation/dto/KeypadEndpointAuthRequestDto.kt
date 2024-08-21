package bob.e2e.presentation.dto

data class KeypadEndpointAuthRequestDto (
    val userInput: String,
    val keyHashMap: Map<String, String>
)