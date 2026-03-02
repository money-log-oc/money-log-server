package oc.moneylog.server.service

import oc.moneylog.server.domain.Transaction
import oc.moneylog.server.store.InMemoryStore
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import java.time.LocalDateTime

class TransactionServiceTest {
    private fun storeWithFixtures(): InMemoryStore {
        val store = InMemoryStore()
        store.transactions.clear()
        store.transactions += Transaction(1, LocalDateTime.of(2026,3,2,10,0), "A", 10_000, mutableSetOf("음식"))
        store.transactions += Transaction(2, LocalDateTime.of(2026,3,2,11,0), "B", 5_000, mutableSetOf("카페"))
        store.transactions += Transaction(3, LocalDateTime.of(2026,3,3,12,0), "C", 7_000, mutableSetOf(), excluded = false)
        store.transactions += Transaction(4, LocalDateTime.of(2026,3,3,13,0), "D", 9_000, mutableSetOf("교통"), excluded = true)
        return store
    }

    @Test
    fun `monthly tag report should include unclassified and exclude excluded`() {
        val svc = TransactionService(storeWithFixtures())
        val report = svc.monthlyTagReport("2026-03")
        assertEquals(10_000, report["음식"])
        assertEquals(5_000, report["카페"])
        assertEquals(7_000, report["미분류"])
        assertTrue("교통" !in report.keys)
    }

    @Test
    fun `update excluded should set reason`() {
        val store = storeWithFixtures()
        val svc = TransactionService(store)
        val tx = svc.updateExcluded(1, true, "TRANSFER")
        assertTrue(tx.excluded)
        assertEquals("TRANSFER", tx.exclusionReason)
    }
}
