package oc.moneylog.server.service

import oc.moneylog.server.domain.Transaction
import oc.moneylog.server.application.port.out.TransactionPort
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class TransactionService(
    private val transactionPort: TransactionPort,
) {
    fun list(month: String?, unclassifiedOnly: Boolean): List<Transaction> {
        val source = loadAll()
        val filtered = source.asSequence().filter { tx ->
            val byMonth = if (month.isNullOrBlank()) true else tx.occurredAt.toLocalDate().toString().startsWith(month)
            val byClass = if (unclassifiedOnly) tx.tags.isEmpty() && !tx.excluded else true
            byMonth && byClass
        }
        return filtered.sortedByDescending { it.occurredAt }.toList()
    }

    fun updateTags(transactionId: Long, tags: List<String>): Transaction {
        val tx = transactionPort.findById(transactionId)
            ?: throw IllegalArgumentException("transaction not found: $transactionId")
        tx.tags = tags.filter { it.isNotBlank() }.toMutableSet()
        tx.excluded = false
        tx.exclusionReason = null
        return transactionPort.save(tx)
    }

    fun updateExcluded(transactionId: Long, excluded: Boolean, reason: String?): Transaction {
        val tx = transactionPort.findById(transactionId)
            ?: throw IllegalArgumentException("transaction not found: $transactionId")
        tx.excluded = excluded
        tx.exclusionReason = if (excluded) reason ?: "MANUAL" else null
        return transactionPort.save(tx)
    }

    fun monthlyTagReport(month: String): Map<String, Long> {
        return list(month, false)
            .asSequence()
            .filter { !it.excluded }
            .flatMap { tx ->
                if (tx.tags.isEmpty()) sequenceOf("미분류" to tx.amount)
                else tx.tags.asSequence().map { it to tx.amount }
            }
            .groupingBy { it.first }
            .fold(0L) { acc, item -> acc + item.second }
            .toList()
            .sortedByDescending { it.second }
            .toMap()
    }

    fun dailySpending(month: String): List<Triple<LocalDate, Long, Long>> {
        val (start, end) = AllowanceCalculator.dailyRange(month)
        val grouped = list(month, false)
            .asSequence()
            .filter { !it.excluded }
            .groupBy { it.occurredAt.toLocalDate() }

        return generateSequence(start) { d -> if (d < end) d.plusDays(1) else null }
            .map { date ->
                val expense = grouped[date].orEmpty().sumOf { it.amount }
                Triple(date, 0L, expense)
            }
            .toList()
    }

    private fun loadAll(): MutableList<Transaction> = transactionPort.findAll().toMutableList()
}
