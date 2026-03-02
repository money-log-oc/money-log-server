package oc.moneylog.server.store

import oc.moneylog.server.domain.BudgetSettings
import oc.moneylog.server.domain.Transaction
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicLong

@Component
class InMemoryStore {
    val budgetSettings: BudgetSettings = BudgetSettings()
    private val idGen = AtomicLong(1000)

    val transactions: MutableList<Transaction> = mutableListOf(
        Transaction(idGen.incrementAndGet(), LocalDateTime.now().minusDays(1), "스타벅스", 6_300, mutableSetOf("카페")),
        Transaction(idGen.incrementAndGet(), LocalDateTime.now().minusDays(1), "다이소", 6_000, mutableSetOf("생활")),
        Transaction(idGen.incrementAndGet(), LocalDateTime.now().minusHours(5), "카카오T", 7_800, mutableSetOf("교통")),
        Transaction(idGen.incrementAndGet(), LocalDateTime.now().minusHours(2), "파리바게뜨", 2_700),
        Transaction(idGen.incrementAndGet(), LocalDateTime.now().minusDays(3), "계좌이체", 50_000, excluded = true, exclusionReason = "TRANSFER"),
    )
}
