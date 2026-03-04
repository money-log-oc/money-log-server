package oc.moneylog.server.adapter.`in`.web

import com.fasterxml.jackson.databind.ObjectMapper
import oc.moneylog.server.adapter.`in`.web.budget.BudgetController
import oc.moneylog.server.application.budget.BudgetUseCase
import oc.moneylog.server.domain.BudgetSettings
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(BudgetController::class)
class BudgetControllerWebMvcTest {
    @Autowired lateinit var mockMvc: MockMvc
    @Autowired lateinit var objectMapper: ObjectMapper

    @MockBean lateinit var budgetUseCase: BudgetUseCase

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
        val body = mapOf("paydayDay" to 25, "monthlyBudget" to 800000)
        whenever(budgetUseCase.updateSettings(25, 800000)).thenReturn(BudgetSettings(25, 800000, "MONDAY"))

        mockMvc.perform(
            put("/api/settings/budget")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.monthlyBudget").value(800000))
    }


    @Test
    fun `invalid budget payload returns 400`() {
        val body = mapOf("paydayDay" to 31, "monthlyBudget" to -1)

        mockMvc.perform(
            put("/api/settings/budget")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(body)),
        )
            .andExpect(status().isBadRequest)
    }

}
