package oc.moneylog.server.service

import oc.moneylog.server.store.InMemoryStore
import kotlin.test.Test
import kotlin.test.assertFailsWith

class BudgetServiceTest {
    private val service = BudgetService(InMemoryStore())

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
