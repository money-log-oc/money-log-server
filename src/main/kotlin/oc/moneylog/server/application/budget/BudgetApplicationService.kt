package oc.moneylog.server.application.budget

import oc.moneylog.server.application.port.out.BudgetSettingsPort
import oc.moneylog.server.domain.BudgetSettings
import org.springframework.stereotype.Service

@Service
class BudgetApplicationService(
    private val budgetSettingsPort: BudgetSettingsPort,
) : BudgetUseCase {
    override fun getSettings(): BudgetSettings =
        budgetSettingsPort.findCurrent() ?: BudgetSettings()

    override fun updateSettings(paydayDay: Int, monthlyBudget: Long): BudgetSettings {
        require(paydayDay in 1..28) { "paydayDay must be between 1 and 28" }
        require(monthlyBudget > 0) { "monthlyBudget must be positive" }

        val next = BudgetSettings(paydayDay = paydayDay, monthlyBudget = monthlyBudget, weekStart = "MONDAY")
        return budgetSettingsPort.save(next)
    }
}
