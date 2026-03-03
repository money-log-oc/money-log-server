package oc.moneylog.server.auth

import jakarta.validation.constraints.NotBlank

data class KakaoLoginRequest(
    @field:NotBlank
    val accessToken: String,
)

data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val userId: String,
)
