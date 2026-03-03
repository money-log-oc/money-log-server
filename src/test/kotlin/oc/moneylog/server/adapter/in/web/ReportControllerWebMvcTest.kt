package oc.moneylog.server.adapter.`in`.web

import oc.moneylog.server.adapter.`in`.web.report.ReportController
import oc.moneylog.server.application.transaction.TransactionUseCase
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDate

@WebMvcTest(ReportController::class)
class ReportControllerWebMvcTest {
    @Autowired lateinit var mockMvc: MockMvc

    @MockBean lateinit var transactionUseCase: TransactionUseCase

    @Test
    fun `monthly tag report returns 200`() {
        whenever(transactionUseCase.monthlyTagReport("2026-03")).thenReturn(mapOf("음식" to 112000L))

        mockMvc.perform(get("/api/reports/monthly-tags").param("month", "2026-03"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].tag").value("음식"))
    }

    @Test
    fun `daily spending returns 200`() {
        whenever(transactionUseCase.dailySpending("2026-03")).thenReturn(listOf(Triple(LocalDate.of(2026,3,2), 0L, 32500L)))

        mockMvc.perform(get("/api/reports/daily-spending").param("month", "2026-03"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].expense").value(32500))
    }
}
