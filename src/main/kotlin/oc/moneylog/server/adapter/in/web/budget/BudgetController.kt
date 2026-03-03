package oc.moneylog.server.adapter.`in`.web.budget

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import oc.moneylog.server.dto.BudgetSettingsResponse
import oc.moneylog.server.dto.BudgetSettingsUpdateRequest
import oc.moneylog.server.service.BudgetService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/settings/budget")
class BudgetController(
    private val budgetService: BudgetService,
) {
    @Operation(summary = "예산 설정 조회")
    @GetMapping
    fun getBudgetSettings(): BudgetSettingsResponse {
        val s = budgetService.getSettings()
        return BudgetSettingsResponse(s.paydayDay, s.monthlyBudget, s.weekStart)
    }

    @Operation(summary = "예산 설정 수정")
    @PutMapping
    fun updateBudgetSettings(@Valid @RequestBody req: BudgetSettingsUpdateRequest): BudgetSettingsResponse {
        val s = budgetService.updateSettings(req.paydayDay, req.monthlyBudget)
        return BudgetSettingsResponse(s.paydayDay, s.monthlyBudget, s.weekStart)
    }
}
