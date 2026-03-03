package oc.moneylog.server.application.transaction

import oc.moneylog.server.domain.Transaction
import java.time.LocalDate

interface TransactionUseCase {
    fun list(month: String?, unclassifiedOnly: Boolean): List<Transaction>
    fun updateTags(transactionId: Long, tags: List<String>): Transaction
    fun updateExcluded(transactionId: Long, excluded: Boolean, reason: String?): Transaction
    fun monthlyTagReport(month: String): Map<String, Long>
    fun dailySpending(month: String): List<Triple<LocalDate, Long, Long>>
}
