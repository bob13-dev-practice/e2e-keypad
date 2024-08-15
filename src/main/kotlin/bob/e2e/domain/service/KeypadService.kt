package bob.e2e.domain.service

import bob.e2e.presentation.dto.KeypadResponseDto
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Service
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.MessageDigest
import java.util.*
import javax.imageio.ImageIO

@Service
class KeypadService {

    fun getKeypad(): KeypadResponseDto {
        //* 1. 키패드 map(0~9)에 매핑되는 해시값(map) 생성 => hashes
        val hashes: Map<String, String> = (0..9).map { it.toString() }.associateWith {
            MessageDigest.getInstance("SHA-256").digest(it.toString().toByteArray()).joinToString("") { byte -> "%02x".format(byte) }
        }

        //* 2. EMPTY 2개 포함해서 0~9를 셔플하고 keys 배열 생성 -> 리턴되는 버튼 이미지 순서
        val keys = (0..9).map { it.toString() } + listOf("EMPTY", "EMPTY")
        val shuffledKeys = keys.shuffled(Random())

        //* 3. shuffledNum을 순회하면서 그 값에 해당하는 해시값들에 대한 배열 생성
        val hashKeys: List<String> = shuffledKeys.map{ key ->
            if (key == "EMPTY") {
                ""
            } else {
                hashes[key] ?: ""
            }
        }

        //* 5. 키패드 이미지 Map 생성 -> num: imagePath
        val resource = ClassPathResource("keypad-img")
        val imagePathMap: Map<String, String> = resource.file.listFiles()
            ?.filter { it.extension == "png" } // PNG 파일만 필터링
            ?.sortedBy { it.name.substringAfter('_').substringBefore('.') }
            ?.associate { file ->
                val key = file.name.substringAfter('_').substringBefore('.')
                key to file.absolutePath // 키와 절대 경로를 쌍으로 저장
            } ?: emptyMap()

        //6. 하나의 이미지로 생성
        val rows = 3
        val cols = 4
        val imageWidth = 50 // 각 이미지의 너비
        val imageHeight = 50 // 각 이미지의 높이

        val mergedImage = BufferedImage(imageWidth * cols, imageHeight * rows, BufferedImage.TYPE_INT_ARGB)
        val g: Graphics2D = mergedImage.createGraphics()

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val index = i * cols + j
                val hashIndex = hashes[index.toString()]
                if (index < shuffledKeys.size) {
                    val imagePath = imagePathMap.get(shuffledKeys[index])
                    if (imagePath != null) {
                        val image = ImageIO.read(File(imagePath))
                        g.drawImage(image, j * imageWidth, i * imageHeight, imageWidth, imageHeight, null)
                    }
                }
            }
        }

        g.dispose()

        // BufferedImage를 Base64로 인코딩
        val byteArrayOutputStream = ByteArrayOutputStream()
        ImageIO.write(mergedImage, "png", byteArrayOutputStream) // 이미지를 PNG 형식으로 ByteArrayOutputStream에 저장
        val imageBytes = byteArrayOutputStream.toByteArray() // 바이트 배열로 변환

        // Base64 인코딩
        val base64String = Base64.getEncoder().encodeToString(imageBytes)

        val keypadDetails: Triple<UUID, Long, String> = generateKeypadDetails("jiyun");

        return KeypadResponseDto(keypadDetails.first, hashKeys, keypadDetails.second, base64String);
    }

    fun generateKeypadDetails(secretKey: String): Triple<UUID, Long, String> {
        val keypadId = UUID.randomUUID() // 키패드 ID 생성
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