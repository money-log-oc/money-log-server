package oc.moneylog.server.infrastructure.security

import oc.moneylog.server.adapter.`in`.web.home.HomeController
import oc.moneylog.server.application.home.HomeSummaryUseCase
import oc.moneylog.server.dto.CycleRange
import oc.moneylog.server.dto.HomeSummaryResponse
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(HomeController::class)
@Import(SecurityConfig::class, SecurityIntegrationWebMvcTest.SecurityTestBeans::class)
class SecurityIntegrationWebMvcTest {

    @TestConfiguration
    class SecurityTestBeans {
        @Bean
        fun jwtTokenProvider() = JwtTokenProvider()
    }

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var homeSummaryUseCase: HomeSummaryUseCase

    @Test
    fun `protected endpoint without bearer token should return 401`() {
        mockMvc.perform(get("/api/home/summary"))
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `protected endpoint with valid bearer token should return 200`() {
        whenever(homeSummaryUseCase.get()).thenReturn(
            HomeSummaryResponse(
                monthlyBudget = 700000,
                weeklySpent = 92000,
                weeklyLimit = 110000,
                livingAccountBalance = 438200,
                cycle = CycleRange("2026-02-25T00:00", "2026-03-25T00:00"),
            ),
        )

        mockMvc.perform(
            get("/api/home/summary")
                .header("Authorization", "Bearer ml_access_abcdef"),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.weeklyLimit").value(110000))
    }
}
