package oc.moneylog.server.application

import oc.moneylog.server.application.port.out.TransactionPort
import oc.moneylog.server.application.transaction.TransactionApplicationService
import oc.moneylog.server.domain.Transaction
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDateTime
import kotlin.test.assertEquals

class TransactionApplicationServiceTest {
    private val transactionPort: TransactionPort = mock()
    private val sut = TransactionApplicationService(transactionPort)

    @Test
    fun `list reads from port`() {
        val rows = listOf(Transaction(1, LocalDateTime.of(2026, 3, 1, 10, 0), "A", 1000))
        whenever(transactionPort.findAll()).thenReturn(rows)

        val out = sut.list("2026-03", false)

        assertEquals(1, out.size)
        verify(transactionPort).findAll()
    }
}
