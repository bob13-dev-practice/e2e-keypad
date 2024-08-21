package bob.e2e.controller

import bob.e2e.service.KeypadService
import bob.e2e.presentation.dto.KeypadAuthRequestDto
import bob.e2e.presentation.dto.KeypadResponseDto
import org.springframework.web.bind.annotation.*

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

    @PostMapping("/auth")
    fun authKeypad(@RequestBody requestDto: KeypadAuthRequestDto): String  {
        return keypadService.authKeypad(requestDto)
    }
}