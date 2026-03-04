package oc.moneylog.server.auth

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank

@Schema(description = "카카오 로그인 요청")
data class KakaoLoginRequest(
    @field:NotBlank
    @field:Schema(description = "카카오 access token", example = "kakao-access-token")
    val accessToken: String,
)

@Schema(description = "인증 응답")
data class AuthResponse(
    @field:Schema(example = "ml_access_xxxxxx")
    val accessToken: String,
    @field:Schema(example = "ml_refresh_xxxxxx")
    val refreshToken: String,
    @field:Schema(example = "kakao_xxxxxx")
    val userId: String,
)

@Schema(description = "토큰 재발급 요청")
data class RefreshTokenRequest(
    @field:NotBlank
    @field:Schema(example = "<refresh-jwt-token>")
    val refreshToken: String,
)
