# Hexagonal Architecture Plan (updated)

## Implemented
- Domain-based controllers under `adapter/in/web/*`
- Application usecases for home/budget/transaction/auth
- Output ports:
  - `BudgetSettingsPort`
  - `TransactionPort`
- Persistence adapters:
  - `BudgetSettingsPersistenceAdapter`
  - `TransactionPersistenceAdapter`

## Next
1. Remove now-unused `service/*` classes and migrate tests to use `application/*` + ports
2. Keep adapters thin (mapping only)
3. Add integration tests per adapter-out
4. Remove legacy `/api/v1/*` after app route migration

## Latest progress (2026-03-04)
- Moved budget/transaction business logic into:
  - `application/budget/BudgetApplicationService`
  - `application/transaction/TransactionApplicationService`
- Moved allowance calculations from `service/AllowanceCalculator` to `application/AllowanceCalculator`.
- `application/home/HomeSummaryService` now depends on `BudgetUseCase` instead of `BudgetService`.
- Removed legacy `service/*` implementations (`BudgetService`, `TransactionService`, `AllowanceCalculator`).
- Updated unit tests to target application/port contracts.
