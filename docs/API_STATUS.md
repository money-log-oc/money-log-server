# API Status

## Active routes (domain-based)
- GET `/api/settings/budget`
- PUT `/api/settings/budget`
- GET `/api/home/summary`
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
