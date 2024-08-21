package bob.e2e.service

import bob.e2e.global.RedisService
import bob.e2e.presentation.dto.KeypadAuthRequestDto
import bob.e2e.presentation.dto.KeypadEndpointAuthRequestDto
import bob.e2e.presentation.dto.KeypadResponseDto
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.server.ResponseStatusException
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.io.File
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.imageio.ImageIO

@Service
class KeypadService(
    private val redisService: RedisService,
) {

    fun getKeypad(): KeypadResponseDto {
        //* 1. 키패드 map(0~9)에 매핑되는 해시값(map) 생성
        val keyHashMap: Map<String, String> = (0..9).map { it.toString() }.associateWith {
            MessageDigest.getInstance("MD5").digest(it.toString().toByteArray()).joinToString("") { byte -> "%02x".format(byte) }
        }

        redisService.setKeyHashMap("keyHashMap", keyHashMap);

        //* 2. EMPTY 2개 포함해서 0~9를 셔플하고 keys 배열 생성 -> 리턴되는 버튼 이미지 순서
        val keys = (0..9).map { it.toString() } + listOf("EMPTY", "EMPTY")
        val shuffledKeys = keys.shuffled(Random())

        //* 3. shuffledNum을 순회하면서 그 값에 해당하는 해시값들에 대한 배열 생성
        val hashKeys: List<String> = shuffledKeys.map{ key ->
            if (key == "EMPTY") {
                ""
            } else {
                keyHashMap[key] ?: ""
            }
        }

        //* 4. 키패드 이미지 Map 생성 -> num: imagePath
        val resource = ClassPathResource("keypad-img")
        val imagePathMap: Map<String, String> = resource.file.listFiles()
            ?.filter { it.extension == "png" } // PNG 파일만 필터링
            ?.sortedBy { it.name.substringAfter('_').substringBefore('.') }
            ?.associate { file ->
                val key = file.name.substringAfter('_').substringBefore('.')
                key to file.absolutePath // 키와 절대 경로를 쌍으로 저장
            } ?: emptyMap()

        //* 5. 하나의 키패드 이미지 생성
        val rows = 3
        val cols = 4
        val imageWidth = 50
        val imageHeight = 50

        val mergedImage = BufferedImage(imageWidth * cols, imageHeight * rows, BufferedImage.TYPE_INT_ARGB)
        val g: Graphics2D = mergedImage.createGraphics()

        for (i in 0 until rows) {
            for (j in 0 until cols) {
                val index = i * cols + j
                val hashIndex = keyHashMap[index.toString()]
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
        val keypadImage = Base64.getEncoder().encodeToString(imageBytes)

        //* 7. keyPadId, timestamp, hash 생성
        val secretKey = "jiyun"
        redisService.setSecretKey("secretKey", secretKey)

        val (keypadId, timestamp, hash) = generateKeypadDetails(secretKey);

        return KeypadResponseDto(keypadId, timestamp, hash, hashKeys, keypadImage);
    }

    fun authKeypad(requestDto: KeypadAuthRequestDto): String {
        val reqKeypadId = requestDto.id;
        val reqTimestamp = requestDto.timestamp;
        val reqHash = requestDto.hash;

        //* 1. 해시값 생성
        val secretKey = redisService.getSecretKey("secretKey")
        val hash = doHash("$reqKeypadId$reqTimestamp$secretKey")

        //* 2. 해시값 비교
        if (reqHash != hash) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "해시값이 일치하지 않습니다.")
        }

        val keyHashMap: Map<String, String> = redisService.getKeyHashMap("keyHashMap")
        val reqUserInput = requestDto.userInput;
        val requestDto = KeypadEndpointAuthRequestDto(reqUserInput, keyHashMap)

        //* 3. restClient를 통해 EndPoint로 post 요청
        val restClient = RestClient.create()

        return restClient.post()
            .uri("http://146.56.119.112:8081/auth")
            .accept(MediaType.APPLICATION_JSON)
            .body(requestDto)
            .retrieve()
            .body(String::class.java)
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