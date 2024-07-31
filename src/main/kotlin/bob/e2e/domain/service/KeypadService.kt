package bob.e2e.domain.service

import bob.e2e.presentation.dto.KeypadResponseDto
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.util.*

@Service
class KeypadService {

    fun getKeypad(): KeypadResponseDto {

        //* 키패드는 유효시간이 지나면 만료되어야 한다.
        val (keypadId, timestamp, hash) = generateKeypadDetails(secretKey)

    }

    fun generateKeypadDetails(secretKey: String): Triple<String, Long, String> {
        val keypadId = UUID.randomUUID().toString() // 키패드 ID 생성
        val timestamp = System.currentTimeMillis() // 현재 타임스탬프
        val hash = doHash("$keypadId$timestamp$secretKey") // 해시 생성

        return Triple(keypadId, timestamp, hash) // Triple 형태로 반환
    }

    fun doHash(value: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashBytes = digest.digest(value.toByteArray())
        return Base64.getEncoder().encodeToString(hashBytes)
    }
}