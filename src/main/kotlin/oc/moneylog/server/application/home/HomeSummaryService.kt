package oc.moneylog.server.application.home

import oc.moneylog.server.application.AllowanceCalculator
import oc.moneylog.server.application.budget.BudgetUseCase
import oc.moneylog.server.dto.CycleRange
import oc.moneylog.server.dto.HomeSummaryResponse
import oc.moneylog.server.store.InMemoryStore
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class HomeSummaryService(
    private val budgetUseCase: BudgetUseCase,
    private val store: InMemoryStore,
) : HomeSummaryUseCase {
    override fun get(): HomeSummaryResponse {
        val now = LocalDateTime.now()
        val s = budgetUseCase.getSettings()
        val (start, end) = AllowanceCalculator.cycleRange(now, s.paydayDay)
        return HomeSummaryResponse(
            monthlyBudget = s.monthlyBudget,
            weeklySpent = AllowanceCalculator.weeklySpent(store.transactions, now),
            weeklyLimit = AllowanceCalculator.weeklyLimit(s, store.transactions, now),
            livingAccountBalance = 438_200,
            cycle = CycleRange(start.toString(), end.toString()),
        )
    }
}
