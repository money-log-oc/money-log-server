package oc.moneylog.server.adapter.`in`.web.auth

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import oc.moneylog.server.application.auth.KakaoAuthUseCase
import oc.moneylog.server.auth.AuthResponse
import oc.moneylog.server.auth.KakaoLoginRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "auth")
@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val kakaoAuthUseCase: KakaoAuthUseCase,
) {
    @Operation(summary = "카카오 로그인")
    @PostMapping("/kakao")
    fun kakaoLogin(@Valid @RequestBody req: KakaoLoginRequest): AuthResponse =
        kakaoAuthUseCase.loginWithAccessToken(req.accessToken)
}
