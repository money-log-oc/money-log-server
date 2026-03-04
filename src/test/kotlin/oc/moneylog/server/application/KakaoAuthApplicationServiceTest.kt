package oc.moneylog.server.application

import oc.moneylog.server.application.auth.KakaoAuthApplicationService
import oc.moneylog.server.application.auth.KakaoTokenInfo
import oc.moneylog.server.application.auth.KakaoTokenVerifier
import oc.moneylog.server.infrastructure.security.JwtTokenProvider
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class KakaoAuthApplicationServiceTest {
    private val verifier: KakaoTokenVerifier = mock()
    private val jwtTokenProvider = JwtTokenProvider(
        rawSecret = "test-secret-key-should-be-at-least-32bytes-long",
        accessTokenExpSeconds = 1800,
        refreshTokenExpSeconds = 1209600,
    )
    private val sut = KakaoAuthApplicationService(verifier, jwtTokenProvider)

    @Test
    fun `loginWithAccessToken verifies kakao token and issues jwt pair`() {
        whenever(verifier.verify("kakao-token")).thenReturn(KakaoTokenInfo("kakao_123456"))

        val out = sut.loginWithAccessToken("kakao-token")

        assertEquals("kakao_123456", out.userId)
        assertTrue(out.accessToken.isNotBlank())
        assertTrue(out.refreshToken.isNotBlank())
        assertTrue(jwtTokenProvider.authenticateIfValid(out.accessToken))
    }

    @Test
    fun `reissue issues new token pair from valid refresh token`() {
        whenever(verifier.verify("kakao-token")).thenReturn(KakaoTokenInfo("kakao_123456"))
        val login = sut.loginWithAccessToken("kakao-token")

        val reissued = sut.reissue(login.refreshToken)

        assertEquals("kakao_123456", reissued.userId)
        assertTrue(jwtTokenProvider.authenticateIfValid(reissued.accessToken))
        assertTrue(reissued.refreshToken.isNotBlank())
    }

    @Test
    fun `reissue throws on invalid refresh token`() {
        assertFailsWith<IllegalArgumentException> {
            sut.reissue("invalid-refresh-token")
        }
    }
}
