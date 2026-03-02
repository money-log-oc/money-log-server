package oc.moneylog.server.domain

import java.time.LocalDate
import java.time.LocalDateTime

data class BudgetSettings(
    var paydayDay: Int = 25,
    var monthlyBudget: Long = 700_000,
    var weekStart: String = "MONDAY",
)

data class Transaction(
    val id: Long,
    val occurredAt: LocalDateTime,
    val merchant: String,
    val amount: Long,
    var tags: MutableSet<String> = mutableSetOf(),
    var excluded: Boolean = false,
    var exclusionReason: String? = null,
)

data class DailySpending(
    val date: LocalDate,
    val income: Long,
    val expense: Long,
)
