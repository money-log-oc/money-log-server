package oc.moneylog.server.service

import oc.moneylog.server.domain.BudgetSettings
import oc.moneylog.server.persistence.entity.BudgetSettingsEntity
import oc.moneylog.server.persistence.repo.BudgetSettingsRepository
import oc.moneylog.server.store.InMemoryStore
import org.springframework.stereotype.Service

@Service
class BudgetService(
    private val store: InMemoryStore,
    private val repository: BudgetSettingsRepository? = null,
) {
    fun getSettings(): BudgetSettings {
        val entity = repository?.findAll()?.firstOrNull()
        return if (entity != null) {
            BudgetSettings(entity.paydayDay, entity.monthlyBudget, entity.weekStart)
        } else {
            store.budgetSettings
        }
    }

    fun updateSettings(paydayDay: Int, monthlyBudget: Long): BudgetSettings {
        require(paydayDay in 1..28) { "paydayDay must be between 1 and 28" }
        require(monthlyBudget > 0) { "monthlyBudget must be positive" }

        store.budgetSettings.paydayDay = paydayDay
        store.budgetSettings.monthlyBudget = monthlyBudget

        repository?.let { repo ->
            val current = repo.findAll().firstOrNull() ?: BudgetSettingsEntity()
            current.paydayDay = paydayDay
            current.monthlyBudget = monthlyBudget
            current.weekStart = "MONDAY"
            repo.save(current)
        }

        return store.budgetSettings
    }
}
