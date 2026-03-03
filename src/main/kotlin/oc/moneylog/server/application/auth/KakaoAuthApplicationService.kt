package oc.moneylog.server.application.auth

import oc.moneylog.server.auth.AuthResponse
import org.springframework.stereotype.Service

@Service
class KakaoAuthApplicationService : KakaoAuthUseCase {
    override fun loginWithAccessToken(accessToken: String): AuthResponse {
        // TODO: real kakao token validation + user upsert + jwt issuance
        val pseudo = accessToken.takeLast(6).ifBlank { "guest00" }
        return AuthResponse(
            accessToken = "ml_access_$pseudo",
            refreshToken = "ml_refresh_$pseudo",
            userId = "kakao_$pseudo",
        )
    }
}
