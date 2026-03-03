package oc.moneylog.server.persistence.repo

import oc.moneylog.server.persistence.entity.TransactionEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface TransactionRepository : JpaRepository<TransactionEntity, Long> {
    fun findAllByOccurredAtBetween(start: LocalDateTime, end: LocalDateTime): List<TransactionEntity>
}
