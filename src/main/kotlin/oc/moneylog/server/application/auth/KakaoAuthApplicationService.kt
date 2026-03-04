package oc.moneylog.server.application.auth

import oc.moneylog.server.auth.AuthResponse
import oc.moneylog.server.infrastructure.security.JwtTokenProvider
import org.springframework.stereotype.Service

@Service
class KakaoAuthApplicationService(
    private val kakaoTokenVerifier: KakaoTokenVerifier,
    private val jwtTokenProvider: JwtTokenProvider,
) : KakaoAuthUseCase {
    override fun loginWithAccessToken(accessToken: String): AuthResponse {
        val kakao = kakaoTokenVerifier.verify(accessToken)
        return AuthResponse(
            accessToken = jwtTokenProvider.createAccessToken(kakao.kakaoUserId),
            refreshToken = jwtTokenProvider.createRefreshToken(kakao.kakaoUserId),
            userId = kakao.kakaoUserId,
        )
    }
}
