package oc.moneylog.server.fixture

import oc.moneylog.server.domain.Transaction
import java.time.LocalDateTime

object TransactionFixtures {
    fun tx(
        id: Long = 1,
        daysAgo: Long = 0,
        amount: Long = 10_000,
        tags: Set<String> = setOf("음식"),
        excluded: Boolean = false,
    ): Transaction = Transaction(
        id = id,
        occurredAt = LocalDateTime.now().minusDays(daysAgo),
        merchant = "fixture-$id",
        amount = amount,
        tags = tags.toMutableSet(),
        excluded = excluded,
    )
}
