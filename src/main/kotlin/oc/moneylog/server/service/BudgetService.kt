package oc.moneylog.server.service

import oc.moneylog.server.domain.BudgetSettings
import oc.moneylog.server.store.InMemoryStore
import org.springframework.stereotype.Service

@Service
class BudgetService(
    private val store: InMemoryStore,
) {
    fun getSettings(): BudgetSettings = store.budgetSettings

    fun updateSettings(paydayDay: Int, monthlyBudget: Long): BudgetSettings {
        require(paydayDay in 1..28) { "paydayDay must be between 1 and 28" }
        require(monthlyBudget > 0) { "monthlyBudget must be positive" }

        store.budgetSettings.paydayDay = paydayDay
        store.budgetSettings.monthlyBudget = monthlyBudget
        return store.budgetSettings
    }
}
