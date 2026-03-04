package oc.moneylog.server.application

import oc.moneylog.server.application.report.ReportApplicationService
import oc.moneylog.server.application.transaction.TransactionUseCase
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.time.LocalDate
import kotlin.test.assertEquals

class ReportApplicationServiceTest {
    private val transactionUseCase: TransactionUseCase = mock()
    private val sut = ReportApplicationService(transactionUseCase)

    @Test
    fun `monthly report delegates to transaction usecase`() {
        whenever(transactionUseCase.monthlyTagReport("2026-03")).thenReturn(mapOf("음식" to 1000L))
        val out = sut.monthlyTagReport("2026-03")
        assertEquals(1000L, out["음식"])
        verify(transactionUseCase).monthlyTagReport("2026-03")
    }

    @Test
    fun `daily spending delegates to transaction usecase`() {
        whenever(transactionUseCase.dailySpending("2026-03")).thenReturn(listOf(Triple(LocalDate.of(2026,3,1),0L,100L)))
        val out = sut.dailySpending("2026-03")
        assertEquals(1, out.size)
        verify(transactionUseCase).dailySpending("2026-03")
    }
}
