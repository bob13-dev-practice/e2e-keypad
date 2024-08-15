package bob.e2e.controller

import bob.e2e.domain.service.KeypadService
import bob.e2e.presentation.dto.KeypadResponseDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/keypad")
@RestController
class KeypadController(
    private val keypadService: KeypadService,
) {

    @GetMapping("/test")
    fun testA(): String {
        return "Hello Spring"
    }

    @GetMapping
    fun getKeypad(): KeypadResponseDto {
        return keypadService.getKeypad()
    }
}