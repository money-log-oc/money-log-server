package oc.moneylog.server.application

import oc.moneylog.server.application.budget.BudgetApplicationService
import oc.moneylog.server.domain.BudgetSettings
import oc.moneylog.server.service.BudgetService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class BudgetApplicationServiceTest {
    private val budgetService: BudgetService = mock()
    private val sut = BudgetApplicationService(budgetService)

    @Test
    fun `getSettings delegates to service`() {
        whenever(budgetService.getSettings()).thenReturn(BudgetSettings(25, 700000, "MONDAY"))
        val out = sut.getSettings()
        assertEquals(25, out.paydayDay)
        verify(budgetService).getSettings()
    }
}
