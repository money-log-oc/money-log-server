package oc.moneylog.server.dto

data class BudgetSettingsResponse(
    val paydayDay: Int,
    val monthlyBudget: Long,
    val weekStart: String,
)

data class BudgetSettingsUpdateRequest(
    val paydayDay: Int,
    val monthlyBudget: Long,
)

data class HomeSummaryResponse(
    val monthlyBudget: Long,
    val weeklySpent: Long,
    val weeklyLimit: Long,
    val livingAccountBalance: Long,
    val cycle: CycleRange,
)

data class CycleRange(
    val startAt: String,
    val endAt: String,
)

data class TransactionResponse(
    val id: Long,
    val occurredAt: String,
    val merchant: String,
    val amount: Long,
    val tags: List<String>,
    val excluded: Boolean,
    val exclusionReason: String?,
)

data class TagUpdateRequest(
    val tagIds: List<String>,
)

data class ExcludeUpdateRequest(
    val excluded: Boolean,
    val reason: String? = null,
)

data class TagReportItem(
    val tag: String,
    val amount: Long,
)

data class DailySpendingItem(
    val date: String,
    val income: Long,
    val expense: Long,
)
