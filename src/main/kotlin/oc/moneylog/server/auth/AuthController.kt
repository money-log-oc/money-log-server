package oc.moneylog.server.auth

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController {
    @PostMapping("/kakao")
    fun kakaoLogin(@Valid @RequestBody req: KakaoLoginRequest): AuthResponse {
        // TODO: real kakao token validation + user upsert + jwt issuance
        val pseudo = req.accessToken.takeLast(6).ifBlank { "guest00" }
        return AuthResponse(
            accessToken = "ml_access_$pseudo",
            refreshToken = "ml_refresh_$pseudo",
            userId = "kakao_$pseudo",
        )
    }
}
