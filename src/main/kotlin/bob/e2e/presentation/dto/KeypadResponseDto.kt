package bob.e2e.presentation.dto

import java.util.*

data class KeypadResponseDto(
    val id: UUID,
    val keys: List<String>,
    val timestamp: Long,
    val keypadImage: String,
)




