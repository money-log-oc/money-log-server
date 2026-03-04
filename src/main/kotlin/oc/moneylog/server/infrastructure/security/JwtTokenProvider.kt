package oc.moneylog.server.infrastructure.security

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class JwtTokenProvider {
    fun authenticateIfValid(rawToken: String): Boolean {
        // TODO: replace with real JWT verification logic (signature/expiry/claims)
        if (!rawToken.startsWith("ml_access_")) return false

        val principal = rawToken.removePrefix("ml_access_").ifBlank { "anonymous" }
        val auth = UsernamePasswordAuthenticationToken(
            principal,
            null,
            listOf(SimpleGrantedAuthority("ROLE_USER")),
        )
        SecurityContextHolder.getContext().authentication = auth
        return true
    }
}
