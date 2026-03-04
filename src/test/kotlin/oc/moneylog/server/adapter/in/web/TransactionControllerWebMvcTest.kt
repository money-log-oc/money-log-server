package oc.moneylog.server.adapter.`in`.web

import oc.moneylog.server.adapter.`in`.web.transaction.TransactionController
import oc.moneylog.server.application.transaction.TransactionUseCase
import oc.moneylog.server.domain.Transaction
import oc.moneylog.server.infrastructure.security.JwtTokenProvider
import org.junit.jupiter.api.Test
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

@WebMvcTest(TransactionController::class)
class TransactionControllerWebMvcTest {
    @Autowired lateinit var mockMvc: MockMvc

    @MockitoBean lateinit var transactionUseCase: TransactionUseCase
    @MockitoBean lateinit var jwtTokenProvider: JwtTokenProvider

    @Test
    fun `get transactions returns list`() {
        whenever(transactionUseCase.list("2026-03", false)).thenReturn(
            listOf(Transaction(1, LocalDateTime.now(), "파리바게뜨", 2700)),
        )

        mockMvc.perform(get("/api/transactions").param("month", "2026-03"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].merchant").value("파리바게뜨"))
    }

    @Test
    fun `patch transaction tag updates tag`() {
        val tx = Transaction(1, LocalDateTime.now(), "파리바게뜨", 2700, mutableSetOf("음식"))
        whenever(transactionUseCase.updateTags(1, listOf("음식"))).thenReturn(tx)

        mockMvc.perform(
            patch("/api/transactions/1/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"tagIds":["음식"]}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.tags[0]").value("음식"))
    }


    @Test
    fun `empty tagIds returns 400`() {
        mockMvc.perform(
            patch("/api/transactions/1/tag")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"tagIds":[]}"""),
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `patch transaction exclude updates excluded and reason`() {
        val tx = Transaction(1, LocalDateTime.now(), "파리바게뜨", 2700, mutableSetOf("음식"), excluded = true, exclusionReason = "TRANSFER")
        whenever(transactionUseCase.updateExcluded(1, true, "TRANSFER")).thenReturn(tx)

        mockMvc.perform(
            patch("/api/transactions/1/exclude")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"excluded":true,"reason":"TRANSFER"}"""),
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.excluded").value(true))
            .andExpect(jsonPath("$.exclusionReason").value("TRANSFER"))
    }

    @Test
    fun `patch transaction exclude missing transaction returns 400`() {
        whenever(transactionUseCase.updateExcluded(999, true, null)).thenThrow(IllegalArgumentException("transaction not found: 999"))

        mockMvc.perform(
            patch("/api/transactions/999/exclude")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""{"excluded":true}"""),
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.code").value("BAD_REQUEST"))
    }

}
