package oc.moneylog.server.application.report

import oc.moneylog.server.application.transaction.TransactionUseCase
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ReportApplicationService(
    private val transactionUseCase: TransactionUseCase,
) : ReportUseCase {
    override fun monthlyTagReport(month: String): Map<String, Long> =
        transactionUseCase.monthlyTagReport(month)

    override fun dailySpending(month: String): List<Triple<LocalDate, Long, Long>> =
        transactionUseCase.dailySpending(month)
}
