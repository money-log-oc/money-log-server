package oc.moneylog.server.application.budget

import oc.moneylog.server.domain.BudgetSettings
import oc.moneylog.server.service.BudgetService
import org.springframework.stereotype.Service

@Service
class BudgetApplicationService(
    private val budgetService: BudgetService,
) : BudgetUseCase {
    override fun getSettings(): BudgetSettings = budgetService.getSettings()

    override fun updateSettings(paydayDay: Int, monthlyBudget: Long): BudgetSettings =
        budgetService.updateSettings(paydayDay, monthlyBudget)
}
