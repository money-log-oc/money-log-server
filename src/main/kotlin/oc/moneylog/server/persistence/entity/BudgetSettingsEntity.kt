package oc.moneylog.server.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "budget_settings")
class BudgetSettingsEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    var paydayDay: Int = 25,

    @Column(nullable = false)
    var monthlyBudget: Long = 700_000,

    @Column(nullable = false)
    var weekStart: String = "MONDAY",
)
