package oc.moneylog.server.application.auth

import oc.moneylog.server.auth.AuthResponse

interface KakaoAuthUseCase {
    fun loginWithAccessToken(accessToken: String): AuthResponse
}
