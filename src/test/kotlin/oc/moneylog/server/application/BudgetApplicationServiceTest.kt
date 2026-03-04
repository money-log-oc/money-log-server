package oc.moneylog.server.application

import oc.moneylog.server.application.budget.BudgetApplicationService
import oc.moneylog.server.application.port.out.BudgetSettingsPort
import oc.moneylog.server.domain.BudgetSettings
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

class BudgetApplicationServiceTest {
    private val budgetSettingsPort: BudgetSettingsPort = mock()
    private val sut = BudgetApplicationService(budgetSettingsPort)

    @Test
    fun `getSettings reads from port`() {
        whenever(budgetSettingsPort.findCurrent()).thenReturn(BudgetSettings(25, 700000, "MONDAY"))
        val out = sut.getSettings()
        assertEquals(25, out.paydayDay)
        verify(budgetSettingsPort).findCurrent()
    }
}
