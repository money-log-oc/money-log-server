package oc.moneylog.server.infrastructure.security

import oc.moneylog.server.application.home.HomeSummaryUseCase
import oc.moneylog.server.dto.CycleRange
import oc.moneylog.server.dto.HomeSummaryResponse
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
class SecurityIntegrationWebMvcTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var homeSummaryUseCase: HomeSummaryUseCase

    @Test
    fun `protected endpoint without bearer token should return 403`() {
        mockMvc.perform(get("/api/home/summary"))
            .andExpect(status().isForbidden)
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
