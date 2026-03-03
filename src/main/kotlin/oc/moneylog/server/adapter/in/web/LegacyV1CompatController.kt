package oc.moneylog.server.adapter.`in`.web

import io.swagger.v3.oas.annotations.Hidden
import jakarta.validation.Valid
import oc.moneylog.server.adapter.`in`.web.budget.BudgetController
import oc.moneylog.server.adapter.`in`.web.home.HomeController
import oc.moneylog.server.adapter.`in`.web.report.ReportController
import oc.moneylog.server.adapter.`in`.web.transaction.TransactionController
import oc.moneylog.server.dto.*
import org.springframework.web.bind.annotation.*

@Hidden
@RestController
@RequestMapping("/api/v1")
class LegacyV1CompatController(
    private val budget: BudgetController,
    private val home: HomeController,
    private val tx: TransactionController,
    private val report: ReportController,
) {
    @GetMapping("/settings/budget") fun getBudgetSettings(): BudgetSettingsResponse = budget.getBudgetSettings()
    @PutMapping("/settings/budget") fun updateBudgetSettings(@Valid @RequestBody req: BudgetSettingsUpdateRequest): BudgetSettingsResponse = budget.updateBudgetSettings(req)
    @GetMapping("/home/summary") fun homeSummary(): HomeSummaryResponse = home.homeSummary()
    @GetMapping("/transactions") fun listTransactions(@RequestParam(required = false) month: String?, @RequestParam(defaultValue = "false") unclassified: Boolean): List<TransactionResponse> = tx.listTransactions(month, unclassified)
    @PatchMapping("/transactions/{id}/tag") fun updateTags(@PathVariable id: Long, @Valid @RequestBody req: TagUpdateRequest): TransactionResponse = tx.updateTags(id, req)
    @PatchMapping("/transactions/{id}/exclude") fun updateExclude(@PathVariable id: Long, @RequestBody req: ExcludeUpdateRequest): TransactionResponse = tx.updateExclude(id, req)
    @GetMapping("/reports/monthly-tags") fun monthlyTags(@RequestParam month: String): List<TagReportItem> = report.monthlyTags(month)
    @GetMapping("/reports/daily-spending") fun dailySpending(@RequestParam month: String): List<DailySpendingItem> = report.dailySpending(month)
}
