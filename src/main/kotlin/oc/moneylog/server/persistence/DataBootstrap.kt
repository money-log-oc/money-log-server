package oc.moneylog.server.persistence

import oc.moneylog.server.persistence.entity.BudgetSettingsEntity
import oc.moneylog.server.persistence.entity.TransactionEntity
import oc.moneylog.server.persistence.repo.BudgetSettingsRepository
import oc.moneylog.server.persistence.repo.TransactionRepository
import oc.moneylog.server.store.InMemoryStore
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DataBootstrap {
    @Bean
    fun seedIfEmpty(
        txRepo: TransactionRepository,
        budgetRepo: BudgetSettingsRepository,
        store: InMemoryStore,
    ): CommandLineRunner = CommandLineRunner {
        if (txRepo.count() == 0L) {
            txRepo.saveAll(
                store.transactions.map {
                    TransactionEntity(
                        // id는 DB가 생성하도록 둔다 (IDENTITY)
                        occurredAt = it.occurredAt,
                        merchant = it.merchant,
                        amount = it.amount,
                        tags = it.tags,
                        excluded = it.excluded,
                        exclusionReason = it.exclusionReason,
                    )
                },
            )
        }

        if (budgetRepo.count() == 0L) {
            budgetRepo.save(
                BudgetSettingsEntity(
                    paydayDay = store.budgetSettings.paydayDay,
                    monthlyBudget = store.budgetSettings.monthlyBudget,
                    weekStart = store.budgetSettings.weekStart,
                ),
            )
        }
    }
}
