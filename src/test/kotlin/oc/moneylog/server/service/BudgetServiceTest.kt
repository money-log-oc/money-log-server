package oc.moneylog.server.application.budget

import oc.moneylog.server.application.port.out.BudgetSettingsPort
import oc.moneylog.server.domain.BudgetSettings
import kotlin.test.Test
import kotlin.test.assertFailsWith

class BudgetApplicationValidationTest {
    private val fakePort = object : BudgetSettingsPort {
        override fun findCurrent(): BudgetSettings? = null
        override fun save(settings: BudgetSettings): BudgetSettings = settings
    }

    private val service = BudgetApplicationService(fakePort)

    @Test
    fun `invalid payday throws`() {
        assertFailsWith<IllegalArgumentException> {
            service.updateSettings(31, 700_000)
        }
    }

    @Test
    fun `non-positive budget throws`() {
        assertFailsWith<IllegalArgumentException> {
            service.updateSettings(25, 0)
        }
    }
}
