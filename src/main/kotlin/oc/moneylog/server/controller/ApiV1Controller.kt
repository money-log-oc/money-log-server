package oc.moneylog.server.controller

import jakarta.validation.Valid
import oc.moneylog.server.dto.*
import oc.moneylog.server.application.home.HomeSummaryUseCase
import oc.moneylog.server.service.BudgetService
import oc.moneylog.server.service.TransactionService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1")
class ApiV1Controller(
    private val budgetService: BudgetService,
    private val transactionService: TransactionService,
    private val homeSummaryUseCase: HomeSummaryUseCase,
) {
    @GetMapping("/settings/budget")
    fun getBudgetSettings(): BudgetSettingsResponse {
        val s = budgetService.getSettings()
        return BudgetSettingsResponse(s.paydayDay, s.monthlyBudget, s.weekStart)
    }

    @PutMapping("/settings/budget")
    fun updateBudgetSettings(@Valid @RequestBody req: BudgetSettingsUpdateRequest): BudgetSettingsResponse {
        val s = budgetService.updateSettings(req.paydayDay, req.monthlyBudget)
        return BudgetSettingsResponse(s.paydayDay, s.monthlyBudget, s.weekStart)
    }

    @GetMapping("/home/summary")
    fun homeSummary(): HomeSummaryResponse = homeSummaryUseCase.get()

    @GetMapping("/transactions")
    fun listTransactions(
        @RequestParam(required = false) month: String?,
        @RequestParam(defaultValue = "false") unclassified: Boolean,
    ): List<TransactionResponse> =
        transactionService.list(month, unclassified).map { tx ->
            TransactionResponse(tx.id, tx.occurredAt.toString(), tx.merchant, tx.amount, tx.tags.toList(), tx.excluded, tx.exclusionReason)
        }

    @PatchMapping("/transactions/{id}/tag")
    fun updateTags(@PathVariable id: Long, @Valid @RequestBody req: TagUpdateRequest): TransactionResponse {
        val tx = transactionService.updateTags(id, req.tagIds)
        return TransactionResponse(tx.id, tx.occurredAt.toString(), tx.merchant, tx.amount, tx.tags.toList(), tx.excluded, tx.exclusionReason)
    }

    @PatchMapping("/transactions/{id}/exclude")
    fun updateExclude(@PathVariable id: Long, @RequestBody req: ExcludeUpdateRequest): TransactionResponse {
        val tx = transactionService.updateExcluded(id, req.excluded, req.reason)
        return TransactionResponse(tx.id, tx.occurredAt.toString(), tx.merchant, tx.amount, tx.tags.toList(), tx.excluded, tx.exclusionReason)
    }

    @GetMapping("/reports/monthly-tags")
    fun monthlyTags(@RequestParam month: String): List<TagReportItem> =
        transactionService.monthlyTagReport(month).map { TagReportItem(it.key, it.value) }

    @GetMapping("/reports/daily-spending")
    fun dailySpending(@RequestParam month: String): List<DailySpendingItem> =
        transactionService.dailySpending(month).map { DailySpendingItem(it.first.toString(), it.second, it.third) }
}
