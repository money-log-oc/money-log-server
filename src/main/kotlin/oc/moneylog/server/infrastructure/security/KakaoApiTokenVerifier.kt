package oc.moneylog.server.infrastructure.security

import oc.moneylog.server.application.auth.KakaoTokenInfo
import oc.moneylog.server.application.auth.KakaoTokenVerifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient

@Component
class KakaoApiTokenVerifier(
    @Value("\${kakao.api.base-url:https://kapi.kakao.com}") private val kakaoApiBaseUrl: String,
) : KakaoTokenVerifier {
    private val restClient: RestClient = RestClient.builder()
        .baseUrl(kakaoApiBaseUrl)
        .build()

    override fun verify(accessToken: String): KakaoTokenInfo {
        val body = runCatching {
            restClient.get()
                .uri("/v1/user/access_token_info")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $accessToken")
                .retrieve()
                .body(Map::class.java)
        }.getOrNull() ?: throw IllegalArgumentException("invalid kakao access token")

        val id = body["id"]?.toString()?.trim().orEmpty()
        if (id.isBlank()) throw IllegalArgumentException("invalid kakao access token")

        return KakaoTokenInfo(kakaoUserId = "kakao_$id")
    }
}
