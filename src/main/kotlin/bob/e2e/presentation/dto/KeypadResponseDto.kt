package bob.e2e.presentation.dto

import java.util.*

data class KeypadResponseDto(
    val id: UUID,
    val timestamp: Long,
    val hash: String,
    val keys: List<String>,
    val keypadImage: String
)


