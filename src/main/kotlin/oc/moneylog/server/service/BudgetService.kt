package oc.moneylog.server.service

import oc.moneylog.server.domain.BudgetSettings
import oc.moneylog.server.application.port.out.BudgetSettingsPort
import org.springframework.stereotype.Service

@Service
class BudgetService(
    private val budgetSettingsPort: BudgetSettingsPort,
) {
    fun getSettings(): BudgetSettings =
        budgetSettingsPort.findCurrent() ?: BudgetSettings()

    fun updateSettings(paydayDay: Int, monthlyBudget: Long): BudgetSettings {
        require(paydayDay in 1..28) { "paydayDay must be between 1 and 28" }
        require(monthlyBudget > 0) { "monthlyBudget must be positive" }

        val next = BudgetSettings(paydayDay = paydayDay, monthlyBudget = monthlyBudget, weekStart = "MONDAY")
        return budgetSettingsPort.save(next)
    }
}
