package oc.moneylog.server.infrastructure.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date
import javax.crypto.SecretKey

@Component
class JwtTokenProvider(
    @Value("\${security.jwt.secret:change-this-dev-secret-key-at-least-32bytes}") private val rawSecret: String,
    @Value("\${security.jwt.access-token-exp-seconds:1800}") private val accessTokenExpSeconds: Long,
    @Value("\${security.jwt.refresh-token-exp-seconds:1209600}") private val refreshTokenExpSeconds: Long,
) {
    private val key: SecretKey by lazy {
        val secret = rawSecret.padEnd(32, '0')
        Keys.hmacShaKeyFor(secret.toByteArray(Charsets.UTF_8))
    }

    fun createAccessToken(userId: String): String = createToken(userId = userId, type = "access", expSeconds = accessTokenExpSeconds)

    fun createRefreshToken(userId: String): String = createToken(userId = userId, type = "refresh", expSeconds = refreshTokenExpSeconds)

    fun authenticateIfValid(rawToken: String): Boolean {
        val claims = parseClaims(rawToken) ?: return false
        if (claims["typ"] != "access") return false

        val principal = claims.subject
        val role = claims["role"]?.toString() ?: "USER"
        val auth = UsernamePasswordAuthenticationToken(
            principal,
            null,
            listOf(SimpleGrantedAuthority("ROLE_$role")),
        )
        SecurityContextHolder.getContext().authentication = auth
        return true
    }

    fun reissueFromRefreshToken(refreshToken: String): ReissuedTokens? {
        val claims = parseClaims(refreshToken) ?: return null
        if (claims["typ"] != "refresh") return null
        val userId = claims.subject ?: return null
        return ReissuedTokens(
            userId = userId,
            accessToken = createAccessToken(userId),
            refreshToken = createRefreshToken(userId),
        )
    }

    data class ReissuedTokens(
        val userId: String,
        val accessToken: String,
        val refreshToken: String,
    )

    private fun createToken(userId: String, type: String, expSeconds: Long): String {
        val now = Instant.now()
        return Jwts.builder()
            .subject(userId)
            .claim("typ", type)
            .claim("role", "USER")
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plusSeconds(expSeconds)))
            .signWith(key)
            .compact()
    }

    private fun parseClaims(token: String): Claims? =
        runCatching {
            Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .payload
        }.getOrNull()
}
