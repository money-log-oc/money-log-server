package oc.moneylog.server.infrastructure.security

import org.junit.jupiter.api.AfterEach
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import org.springframework.security.core.context.SecurityContextHolder

class JwtTokenProviderTest {
    private val provider = JwtTokenProvider(
        rawSecret = "test-secret-key-should-be-at-least-32bytes-long",
        accessTokenExpSeconds = 1800,
        refreshTokenExpSeconds = 1209600,
    )

    @AfterEach
    fun clear() {
        SecurityContextHolder.clearContext()
    }

    @Test
    fun `authenticateIfValid accepts valid access token`() {
        val token = provider.createAccessToken("kakao_123456")
        assertTrue(provider.authenticateIfValid(token))
        assertTrue(SecurityContextHolder.getContext().authentication?.isAuthenticated == true)
    }

    @Test
    fun `authenticateIfValid rejects refresh token`() {
        val token = provider.createRefreshToken("kakao_123456")
        assertFalse(provider.authenticateIfValid(token))
    }

    @Test
    fun `authenticateIfValid rejects malformed token`() {
        assertFalse(provider.authenticateIfValid("not-a-jwt"))
    }

    @Test
    fun `reissueFromRefreshToken returns new pair for valid refresh token`() {
        val refresh = provider.createRefreshToken("kakao_123456")
        val reissued = provider.reissueFromRefreshToken(refresh)

        assertTrue(reissued != null)
        assertTrue(provider.authenticateIfValid(reissued!!.accessToken))
        assertTrue(reissued.refreshToken.isNotBlank())
        assertTrue(reissued.userId == "kakao_123456")
    }

    @Test
    fun `reissueFromRefreshToken rejects access token`() {
        val access = provider.createAccessToken("kakao_123456")
        assertTrue(provider.reissueFromRefreshToken(access) == null)
    }
}
