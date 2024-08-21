package bob.e2e.presentation.dto

import java.util.*

data class KeypadAuthRequestDto (
    val id: UUID,
    val timestamp: Long,
    val hash: String,
    val userInput: String
)
