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
1. Move remaining business logic from `service/*` into `application/*` usecases
2. Keep adapters thin (mapping only)
3. Add integration tests per adapter-out
4. Remove legacy `/api/v1/*` after app route migration
