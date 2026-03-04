package oc.moneylog.server.adapter.out.persistence

import oc.moneylog.server.application.port.out.BudgetSettingsPort
import oc.moneylog.server.domain.BudgetSettings
import oc.moneylog.server.persistence.entity.BudgetSettingsEntity
import oc.moneylog.server.persistence.repo.BudgetSettingsRepository
import org.springframework.stereotype.Component

@Component
class BudgetSettingsPersistenceAdapter(
    private val repository: BudgetSettingsRepository,
) : BudgetSettingsPort {
    override fun findCurrent(): BudgetSettings? =
        repository.findAll().firstOrNull()?.let {
            BudgetSettings(
                paydayDay = it.paydayDay,
                monthlyBudget = it.monthlyBudget,
                weekStart = it.weekStart,
            )
        }

    override fun save(settings: BudgetSettings): BudgetSettings {
        val current = repository.findAll().firstOrNull() ?: BudgetSettingsEntity()
        current.paydayDay = settings.paydayDay
        current.monthlyBudget = settings.monthlyBudget
        current.weekStart = settings.weekStart
        val saved = repository.save(current)
        return BudgetSettings(saved.paydayDay, saved.monthlyBudget, saved.weekStart)
    }
}
