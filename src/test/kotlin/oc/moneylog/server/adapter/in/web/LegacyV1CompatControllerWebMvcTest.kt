package oc.moneylog.server.adapter.`in`.web

import oc.moneylog.server.adapter.`in`.web.home.HomeController
import oc.moneylog.server.dto.CycleRange
import oc.moneylog.server.infrastructure.security.JwtTokenProvider
import oc.moneylog.server.dto.HomeSummaryResponse
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(LegacyV1CompatController::class)
class LegacyV1CompatControllerWebMvcTest {
    @Autowired lateinit var mockMvc: MockMvc

    @MockitoBean lateinit var budgetController: oc.moneylog.server.adapter.`in`.web.budget.BudgetController
    @MockitoBean lateinit var homeController: HomeController
    @MockitoBean lateinit var transactionController: oc.moneylog.server.adapter.`in`.web.transaction.TransactionController
    @MockitoBean lateinit var reportController: oc.moneylog.server.adapter.`in`.web.report.ReportController
    @MockitoBean lateinit var jwtTokenProvider: JwtTokenProvider

    @Test
    fun `legacy v1 home summary route should work`() {
        whenever(homeController.homeSummary()).thenReturn(
            HomeSummaryResponse(
                monthlyBudget = 700000,
                weeklySpent = 92000,
                weeklyLimit = 110000,
                livingAccountBalance = 438200,
                cycle = CycleRange("2026-02-25T00:00", "2026-03-25T00:00"),
            )
        )

        mockMvc.perform(get("/api/v1/home/summary"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.monthlyBudget").value(700000))
            .andExpect(jsonPath("$.weeklyLimit").value(110000))
    }
}
