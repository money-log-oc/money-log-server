package oc.moneylog.server.service

import oc.moneylog.server.domain.Transaction
import oc.moneylog.server.persistence.entity.TransactionEntity
import oc.moneylog.server.persistence.repo.TransactionRepository
import oc.moneylog.server.store.InMemoryStore
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class TransactionService(
    private val store: InMemoryStore,
    private val repository: TransactionRepository? = null,
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
        val tx = findById(transactionId)
        tx.tags = tags.filter { it.isNotBlank() }.toMutableSet()
        tx.excluded = false
        tx.exclusionReason = null
        persist(tx)
        return tx
    }

    fun updateExcluded(transactionId: Long, excluded: Boolean, reason: String?): Transaction {
        val tx = findById(transactionId)
        tx.excluded = excluded
        tx.exclusionReason = if (excluded) reason ?: "MANUAL" else null
        persist(tx)
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

    private fun loadAll(): MutableList<Transaction> {
        val rows = repository?.findAll().orEmpty()
        return if (rows.isNotEmpty()) rows.map { it.toDomain() }.toMutableList() else store.transactions
    }

    private fun findById(id: Long): Transaction =
        loadAll().find { it.id == id } ?: throw IllegalArgumentException("transaction not found: $id")

    private fun persist(tx: Transaction) {
        repository?.save(
            TransactionEntity(
                id = tx.id,
                occurredAt = tx.occurredAt,
                merchant = tx.merchant,
                amount = tx.amount,
                tags = tx.tags,
                excluded = tx.excluded,
                exclusionReason = tx.exclusionReason,
            ),
        )
    }

    private fun TransactionEntity.toDomain(): Transaction = Transaction(
        id = id ?: 0,
        occurredAt = occurredAt,
        merchant = merchant,
        amount = amount,
        tags = tags,
        excluded = excluded,
        exclusionReason = exclusionReason,
    )
}
