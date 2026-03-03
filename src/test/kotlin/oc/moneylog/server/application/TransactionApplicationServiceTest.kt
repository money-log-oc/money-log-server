package oc.moneylog.server.application

import oc.moneylog.server.application.transaction.TransactionApplicationService
import oc.moneylog.server.domain.Transaction
import oc.moneylog.server.service.TransactionService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import kotlin.test.assertEquals

class TransactionApplicationServiceTest {
    private val transactionService: TransactionService = mock()
    private val sut = TransactionApplicationService(transactionService)

    @Test
    fun `list delegates to service`() {
        val rows = listOf(Transaction(1, LocalDateTime.now(), "A", 1000))
        whenever(transactionService.list("2026-03", false)).thenReturn(rows)
        val out = sut.list("2026-03", false)
        assertEquals(1, out.size)
        verify(transactionService).list("2026-03", false)
    }
}
