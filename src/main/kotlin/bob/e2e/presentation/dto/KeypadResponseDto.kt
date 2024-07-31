package bob.e2e.presentation.dto

import java.time.LocalDateTime

data class KeypadResponseDto {

    // private val 이미지, 아이디, 시간, 해시값
    val id: Long,
    val keys: List<String>,
    val timestamp: LocalDateTime
}




