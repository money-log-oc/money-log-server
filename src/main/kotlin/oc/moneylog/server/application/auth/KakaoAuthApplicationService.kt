package oc.moneylog.server.application.auth

import oc.moneylog.server.auth.AuthResponse
import oc.moneylog.server.infrastructure.security.JwtTokenProvider
import org.springframework.stereotype.Service

@Service
class KakaoAuthApplicationService(
    private val jwtTokenProvider: JwtTokenProvider,
) : KakaoAuthUseCase {
    override fun loginWithAccessToken(accessToken: String): AuthResponse {
        // TODO: real kakao token validation + user upsert
        val pseudo = accessToken.takeLast(6).ifBlank { "guest00" }
        val userId = "kakao_$pseudo"
        return AuthResponse(
            accessToken = jwtTokenProvider.createAccessToken(userId),
            refreshToken = jwtTokenProvider.createRefreshToken(userId),
            userId = userId,
        )
    }
}
