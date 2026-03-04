package oc.moneylog.server.adapter.`in`.web

import oc.moneylog.server.adapter.`in`.web.auth.AuthController
import oc.moneylog.server.application.auth.KakaoAuthUseCase
import oc.moneylog.server.auth.AuthResponse
import oc.moneylog.server.infrastructure.security.JwtTokenProvider
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(AuthController::class)
class AuthControllerWebMvcTest {
    @Autowired lateinit var mockMvc: MockMvc

    @MockitoBean lateinit var kakaoAuthUseCase: KakaoAuthUseCase
    @MockitoBean lateinit var jwtTokenProvider: JwtTokenProvider

    @Test
    fun `kakao login returns auth response`() {
        whenever(kakaoAuthUseCase.loginWithAccessToken("token")).thenReturn(
            AuthResponse("ml_access_123456", "ml_refresh_123456", "kakao_123456"),
        )

        mockMvc.perform(
            post("/api/auth/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"accessToken":"token"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").value("ml_access_123456"))
            .andExpect(jsonPath("$.userId").value("kakao_123456"))
    }

    @Test
    fun `kakao login validates request`() {
        mockMvc.perform(
            post("/api/auth/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"accessToken":""}"""),
        ).andExpect(status().isBadRequest)
    }

    @Test
    fun `refresh returns reissued auth response`() {
        whenever(kakaoAuthUseCase.reissue("refresh-token")).thenReturn(
            AuthResponse("new-access", "new-refresh", "kakao_123456"),
        )

        mockMvc.perform(
            post("/api/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"refreshToken":"refresh-token"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").value("new-access"))
            .andExpect(jsonPath("$.refreshToken").value("new-refresh"))
    }
}
