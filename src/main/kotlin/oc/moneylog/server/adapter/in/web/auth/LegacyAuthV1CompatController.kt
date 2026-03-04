package oc.moneylog.server.adapter.`in`.web.auth

import io.swagger.v3.oas.annotations.Hidden
import jakarta.validation.Valid
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import oc.moneylog.server.auth.AuthResponse
import oc.moneylog.server.auth.KakaoLoginRequest
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Hidden
@RestController
@ConditionalOnProperty(prefix = "moneylog.api.legacy-v1", name = ["enabled"], havingValue = "true", matchIfMissing = true)
@RequestMapping("/api/v1/auth")
class LegacyAuthV1CompatController(
    private val authController: AuthController,
) {
    @PostMapping("/kakao")
    fun kakaoLogin(@Valid @RequestBody req: KakaoLoginRequest): AuthResponse =
        authController.kakaoLogin(req)
}
