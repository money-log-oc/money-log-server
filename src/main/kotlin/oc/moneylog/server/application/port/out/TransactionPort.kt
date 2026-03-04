package oc.moneylog.server.application.port.out

import oc.moneylog.server.domain.Transaction

interface TransactionPort {
    fun findAll(): List<Transaction>
    fun findById(id: Long): Transaction?
    fun save(transaction: Transaction): Transaction
}
