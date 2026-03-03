package oc.moneylog.server.application.home

import oc.moneylog.server.dto.CycleRange
import oc.moneylog.server.dto.HomeSummaryResponse
import oc.moneylog.server.service.AllowanceCalculator
import oc.moneylog.server.service.BudgetService
import oc.moneylog.server.store.InMemoryStore
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class HomeSummaryService(
    private val budgetService: BudgetService,
    private val store: InMemoryStore,
) : HomeSummaryUseCase {
    override fun get(): HomeSummaryResponse {
        val now = LocalDateTime.now()
        val s = budgetService.getSettings()
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
