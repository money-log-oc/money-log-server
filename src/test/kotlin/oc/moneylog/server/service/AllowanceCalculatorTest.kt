package oc.moneylog.server.application

import oc.moneylog.server.domain.BudgetSettings
import oc.moneylog.server.fixture.TransactionFixtures
import kotlin.test.Test
import kotlin.test.assertTrue
import java.time.LocalDateTime

class AllowanceCalculatorTest {
    @Test
    fun `weekly limit should never be negative even when overspent`() {
        val settings = BudgetSettings(paydayDay = 25, monthlyBudget = 50_000)
        val txs = listOf(
            TransactionFixtures.tx(id = 1, daysAgo = 1, amount = 40_000),
            TransactionFixtures.tx(id = 2, daysAgo = 2, amount = 30_000),
        )

        val limit = AllowanceCalculator.weeklyLimit(settings, txs, LocalDateTime.now())
        assertTrue(limit >= 0)
    }

    @Test
    fun `cycle range should return valid ascending range`() {
        val now = LocalDateTime.of(2026, 3, 2, 10, 0)
        val (start, end) = AllowanceCalculator.cycleRange(now, 25)
        assertTrue(start < end)
    }
}
