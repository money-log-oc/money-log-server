package oc.moneylog.server.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Positive

@Schema(description = "예산 설정 응답")
data class BudgetSettingsResponse(
    @field:Schema(example = "25")
    val paydayDay: Int,
    @field:Schema(example = "700000")
    val monthlyBudget: Long,
    @field:Schema(example = "MONDAY")
    val weekStart: String,
)

@Schema(description = "예산 설정 수정 요청")
data class BudgetSettingsUpdateRequest(
    @field:Min(1)
    @field:Max(28)
    @field:Schema(example = "25")
    val paydayDay: Int,
    @field:Positive
    @field:Schema(example = "700000")
    val monthlyBudget: Long,
)

@Schema(description = "홈 요약 응답")
data class HomeSummaryResponse(
    val monthlyBudget: Long,
    val weeklySpent: Long,
    val weeklyLimit: Long,
    val livingAccountBalance: Long,
    val cycle: CycleRange,
)

@Schema(description = "예산 사이클 범위")
data class CycleRange(
    val startAt: String,
    val endAt: String,
)

@Schema(description = "거래 응답")
data class TransactionResponse(
    val id: Long,
    val occurredAt: String,
    val merchant: String,
    val amount: Long,
    val tags: List<String>,
    val excluded: Boolean,
    val exclusionReason: String?,
)

@Schema(description = "거래 태그 수정 요청")
data class TagUpdateRequest(
    @field:NotEmpty
    val tagIds: List<String>,
)

@Schema(description = "거래 제외 처리 요청")
data class ExcludeUpdateRequest(
    val excluded: Boolean,
    val reason: String? = null,
)

@Schema(description = "태그 리포트 항목")
data class TagReportItem(
    val tag: String,
    val amount: Long,
)

@Schema(description = "일자 리포트 항목")
data class DailySpendingItem(
    val date: String,
    val income: Long,
    val expense: Long,
)

@Schema(description = "표준 에러 응답")
data class ErrorResponse(
    val message: String,
    val code: String,
    val path: String,
    val timestamp: String,
)
