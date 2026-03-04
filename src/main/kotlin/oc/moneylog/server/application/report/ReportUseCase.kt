package oc.moneylog.server.application.report

import java.time.LocalDate

interface ReportUseCase {
    fun monthlyTagReport(month: String): Map<String, Long>
    fun dailySpending(month: String): List<Triple<LocalDate, Long, Long>>
}
