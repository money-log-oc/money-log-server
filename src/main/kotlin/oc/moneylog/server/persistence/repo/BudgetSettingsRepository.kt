package oc.moneylog.server.persistence.repo

import oc.moneylog.server.persistence.entity.BudgetSettingsEntity
import org.springframework.data.jpa.repository.JpaRepository

interface BudgetSettingsRepository : JpaRepository<BudgetSettingsEntity, Long>
