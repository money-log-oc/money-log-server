package oc.moneylog.server.application.port.out

import oc.moneylog.server.domain.BudgetSettings

interface BudgetSettingsPort {
    fun findCurrent(): BudgetSettings?
    fun save(settings: BudgetSettings): BudgetSettings
}
