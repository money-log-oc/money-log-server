package oc.moneylog.server.application.budget

import oc.moneylog.server.domain.BudgetSettings

interface BudgetUseCase {
    fun getSettings(): BudgetSettings
    fun updateSettings(paydayDay: Int, monthlyBudget: Long): BudgetSettings
}
