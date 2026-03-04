package oc.moneylog.server.application.auth

interface KakaoTokenVerifier {
    fun verify(accessToken: String): KakaoTokenInfo
}

data class KakaoTokenInfo(
    val kakaoUserId: String,
)
