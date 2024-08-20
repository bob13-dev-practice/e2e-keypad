package bob.e2e.global

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.ValueOperations
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class RedisService(
    private val jacksonObjectMapper: ObjectMapper,
    private val redisTemplate: RedisTemplate<String, String>
) {
    fun setKeyHashMap(name: String, keyHashMap: Map<String, String>) {
        val values: ValueOperations<String, String> = redisTemplate!!.opsForValue()
        val jsonValue = keyHashMap.let { jacksonObjectMapper.writeValueAsString(it) }
        values.set(name, jsonValue, Duration.ofMinutes(1)) // 1분 뒤 메모리에서 삭제된다.
    }

    fun getKeyHashMap(name: String): Map<String, String> {
        val values: ValueOperations<String, String> = redisTemplate.opsForValue()
        val jsonValue = values[name]
        return jsonValue.let { jacksonObjectMapper.readValue(it, Map::class.java) as Map<String, String> }
    }

    fun setSecretKey(name: String, secretKey: String) {
        val values: ValueOperations<String, String> = redisTemplate!!.opsForValue()
        values.set(name, secretKey, Duration.ofMinutes(1))
    }

    fun getSecretKey(name: String): String {
        val values: ValueOperations<String, String> = redisTemplate!!.opsForValue()
        return values[name]
    }
}