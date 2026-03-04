package oc.moneylog.server.adapter.out.persistence

import oc.moneylog.server.application.port.out.TransactionPort
import oc.moneylog.server.domain.Transaction
import oc.moneylog.server.persistence.entity.TransactionEntity
import oc.moneylog.server.persistence.repo.TransactionRepository
import org.springframework.stereotype.Component

@Component
class TransactionPersistenceAdapter(
    private val repository: TransactionRepository,
) : TransactionPort {
    override fun findAll(): List<Transaction> = repository.findAll().map { it.toDomain() }

    override fun findById(id: Long): Transaction? = repository.findById(id).orElse(null)?.toDomain()

    override fun save(transaction: Transaction): Transaction {
        val saved = repository.save(
            TransactionEntity(
                id = if (transaction.id == 0L) null else transaction.id,
                occurredAt = transaction.occurredAt,
                merchant = transaction.merchant,
                amount = transaction.amount,
                tags = transaction.tags,
                excluded = transaction.excluded,
                exclusionReason = transaction.exclusionReason,
            ),
        )
        return saved.toDomain()
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
