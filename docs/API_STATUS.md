# API Status

## Active routes (domain-based)
- GET `/api/settings/budget` ✅ (Swagger + WebMvc test)
- PUT `/api/settings/budget` ✅ (Swagger + WebMvc test + validation)
- GET `/api/home/summary` ✅ (Swagger + WebMvc test)
- GET `/api/transactions`
- PATCH `/api/transactions/{id}/tag`
- PATCH `/api/transactions/{id}/exclude`
- GET `/api/reports/monthly-tags`
- GET `/api/reports/daily-spending`
- POST `/api/auth/kakao`

## Legacy compatibility routes (temporary)
- `/api/v1/*` mappings are kept for compatibility and will be removed after app migration.

## DoD policy
Each API task is DONE only when:
1) implementation
2) swagger annotations/spec
3) tests (happy + edge)

## API completion snapshot (2026-03-04)
- Budget API (`/api/settings/budget`)
  - implementation: DONE
  - swagger/openapi annotations: DONE
  - tests: DONE (`BudgetControllerWebMvcTest` happy + invalid payload)
