package oc.moneylog.server.application.transaction

import oc.moneylog.server.application.port.out.TransactionPort
import oc.moneylog.server.domain.Transaction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.time.LocalDateTime

class TransactionApplicationServiceBehaviorTest {
    private class FakeTransactionPort(seed: List<Transaction>) : TransactionPort {
        private val rows = seed.toMutableList()

        override fun findAll(): List<Transaction> = rows.toList()

        override fun findById(id: Long): Transaction? = rows.firstOrNull { it.id == id }

        override fun save(transaction: Transaction): Transaction {
            val idx = rows.indexOfFirst { it.id == transaction.id }
            if (idx >= 0) rows[idx] = transaction else rows.add(transaction)
            return transaction
        }
    }

    private fun portWithFixtures(): FakeTransactionPort = FakeTransactionPort(
        listOf(
            Transaction(1, LocalDateTime.of(2026, 3, 2, 10, 0), "A", 10_000, mutableSetOf("음식")),
            Transaction(2, LocalDateTime.of(2026, 3, 2, 11, 0), "B", 5_000, mutableSetOf("카페")),
            Transaction(3, LocalDateTime.of(2026, 3, 3, 12, 0), "C", 7_000, mutableSetOf(), excluded = false),
            Transaction(4, LocalDateTime.of(2026, 3, 3, 13, 0), "D", 9_000, mutableSetOf("교통"), excluded = true),
        )
    )

    @Test
    fun `monthly tag report should include unclassified and exclude excluded`() {
        val svc = TransactionApplicationService(portWithFixtures())
        val report = svc.monthlyTagReport("2026-03")
        assertEquals(10_000, report["음식"])
        assertEquals(5_000, report["카페"])
        assertEquals(7_000, report["미분류"])
        assertTrue("교통" !in report.keys)
    }

    @Test
    fun `update excluded should set reason`() {
        val svc = TransactionApplicationService(portWithFixtures())
        val tx = svc.updateExcluded(1, true, "TRANSFER")
        assertTrue(tx.excluded)
        assertEquals("TRANSFER", tx.exclusionReason)
    }
}
