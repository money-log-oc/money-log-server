package oc.moneylog.server.adapter.`in`.web.report

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import oc.moneylog.server.dto.DailySpendingItem
import oc.moneylog.server.dto.TagReportItem
import oc.moneylog.server.application.report.ReportUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@Tag(name = "report")
@RestController
@RequestMapping("/api/reports")
class ReportController(
    private val reportUseCase: ReportUseCase,
) {
    @Operation(summary = "월별 태그 리포트")
    @GetMapping("/monthly-tags")
    fun monthlyTags(@RequestParam month: String): List<TagReportItem> =
        reportUseCase.monthlyTagReport(month).map { TagReportItem(it.key, it.value) }

    @Operation(summary = "월별 일자 리포트")
    @GetMapping("/daily-spending")
    fun dailySpending(@RequestParam month: String): List<DailySpendingItem> =
        reportUseCase.dailySpending(month).map { DailySpendingItem(it.first.toString(), it.second, it.third) }
}
