package oc.moneylog.server.application.transaction

import oc.moneylog.server.domain.Transaction
import oc.moneylog.server.service.TransactionService
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class TransactionApplicationService(
    private val transactionService: TransactionService,
) : TransactionUseCase {
    override fun list(month: String?, unclassifiedOnly: Boolean): List<Transaction> =
        transactionService.list(month, unclassifiedOnly)

    override fun updateTags(transactionId: Long, tags: List<String>): Transaction =
        transactionService.updateTags(transactionId, tags)

    override fun updateExcluded(transactionId: Long, excluded: Boolean, reason: String?): Transaction =
        transactionService.updateExcluded(transactionId, excluded, reason)

    override fun monthlyTagReport(month: String): Map<String, Long> =
        transactionService.monthlyTagReport(month)

    override fun dailySpending(month: String): List<Triple<LocalDate, Long, Long>> =
        transactionService.dailySpending(month)
}
