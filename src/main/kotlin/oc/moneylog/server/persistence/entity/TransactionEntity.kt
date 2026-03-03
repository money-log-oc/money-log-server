package oc.moneylog.server.persistence.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "transactions")
class TransactionEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var occurredAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    var merchant: String = "",

    @Column(nullable = false)
    var amount: Long = 0,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "transaction_tags", joinColumns = [JoinColumn(name = "transaction_id")])
    @Column(name = "tag", nullable = false)
    var tags: MutableSet<String> = mutableSetOf(),

    @Column(nullable = false)
    var excluded: Boolean = false,

    var exclusionReason: String? = null,
)
