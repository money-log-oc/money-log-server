package oc.moneylog.server.service

import oc.moneylog.server.domain.Transaction
import oc.moneylog.server.store.InMemoryStore
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class TransactionService(
    private val store: InMemoryStore,
) {
    fun list(month: String?, unclassifiedOnly: Boolean): List<Transaction> {
        val filtered = store.transactions.asSequence().filter { tx ->
            val byMonth = if (month.isNullOrBlank()) true else tx.occurredAt.toLocalDate().toString().startsWith(month)
            val byClass = if (unclassifiedOnly) tx.tags.isEmpty() && !tx.excluded else true
            byMonth && byClass
        }
        return filtered.sortedByDescending { it.occurredAt }.toList()
    }

    fun updateTags(transactionId: Long, tags: List<String>): Transaction {
        val tx = findById(transactionId)
        tx.tags = tags.filter { it.isNotBlank() }.toMutableSet()
        tx.excluded = false
        tx.exclusionReason = null
        return tx
    }

    fun updateExcluded(transactionId: Long, excluded: Boolean, reason: String?): Transaction {
        val tx = findById(transactionId)
        tx.excluded = excluded
        tx.exclusionReason = if (excluded) reason ?: "MANUAL" else null
        return tx
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

    private fun findById(id: Long): Transaction =
        store.transactions.find { it.id == id } ?: throw IllegalArgumentException("transaction not found: $id")
}
