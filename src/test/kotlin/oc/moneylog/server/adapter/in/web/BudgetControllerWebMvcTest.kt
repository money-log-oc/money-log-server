package oc.moneylog.server.adapter.`in`.web

import oc.moneylog.server.adapter.`in`.web.budget.BudgetController
import oc.moneylog.server.application.budget.BudgetUseCase
import oc.moneylog.server.domain.BudgetSettings
import oc.moneylog.server.infrastructure.security.JwtTokenProvider
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BudgetController::class)
class BudgetControllerWebMvcTest {
    @Autowired lateinit var mockMvc: MockMvc

    @MockitoBean lateinit var budgetUseCase: BudgetUseCase
    @MockitoBean lateinit var jwtTokenProvider: JwtTokenProvider

    @Test
    fun `get budget settings returns 200`() {
        whenever(budgetUseCase.getSettings()).thenReturn(BudgetSettings(25, 700000, "MONDAY"))

        mockMvc.perform(get("/api/settings/budget"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.paydayDay").value(25))
            .andExpect(jsonPath("$.monthlyBudget").value(700000))
    }

    @Test
    fun `put budget settings validates payload`() {
        whenever(budgetUseCase.updateSettings(25, 800000)).thenReturn(BudgetSettings(25, 800000, "MONDAY"))

        mockMvc.perform(
            put("/api/settings/budget")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"paydayDay":25,"monthlyBudget":800000}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.monthlyBudget").value(800000))
    }


    @Test
    fun `invalid budget payload returns 400`() {
        mockMvc.perform(
            put("/api/settings/budget")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"paydayDay":31,"monthlyBudget":-1}"""),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("VALIDATION_ERROR"))
    }

    @Test
    fun `malformed json returns 400 with standardized code`() {
        mockMvc.perform(
            put("/api/settings/budget")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"paydayDay":25,"monthlyBudget":}"""),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("MALFORMED_JSON"))
    }

}
