# API Status

## Active routes (domain-based)
- GET `/api/settings/budget` ✅ (Swagger + WebMvc test)
- PUT `/api/settings/budget` ✅ (Swagger + WebMvc test + validation)
- GET `/api/home/summary` ✅ (Swagger + WebMvc test)
- GET `/api/transactions` ✅ (Swagger + WebMvc test)
- PATCH `/api/transactions/{id}/tag` ✅ (Swagger + WebMvc test + validation)
- PATCH `/api/transactions/{id}/exclude` ✅ (Swagger + WebMvc test + error case)
- GET `/api/reports/monthly-tags` ✅ (Swagger + WebMvc test + required param validation)
- GET `/api/reports/daily-spending` ✅ (Swagger + WebMvc test + required param validation)
- POST `/api/auth/kakao` ✅ (Kakao 토큰 검증 + JWT 발급)
- POST `/api/auth/refresh` ✅ (refresh 토큰 기반 access/refresh 재발급)

## Legacy compatibility routes (temporary)
- `/api/v1/*` mappings are kept for compatibility and will be removed after app migration.

## DoD policy
Each API task is DONE only when:
1) implementation
2) swagger annotations/spec
3) tests (happy + edge)

## Standard error response/code
- Response shape: `ErrorResponse(message, code, path, timestamp)`
- Error codes:
  - `BAD_REQUEST`
  - `VALIDATION_ERROR`
  - `MISSING_PARAMETER`
  - `MALFORMED_JSON`
  - `INTERNAL_ERROR`

## Security baseline
- Protected APIs: `hasRole("USER")` enforced
- Access token only accepted for API auth (`typ=access`)
- Refresh token cannot access protected APIs (`typ=refresh` denied)

## Data migration baseline
- Flyway enabled (local/dev/prod)
- Baseline migration: `db/migration/V1__init_schema.sql`
- JPA ddl-auto: `validate` (schema managed by Flyway)
- Profile verification (2026-03-04):
  - `SPRING_PROFILES_ACTIVE=local ./gradlew test` ✅
  - `SPRING_PROFILES_ACTIVE=dev ... ./gradlew test` ✅
  - `SPRING_PROFILES_ACTIVE=prod ... ./gradlew test` ✅

## Railway smoke automation
- Script: `scripts/smoke.sh`
- Verified locally (2026-03-04): `./scripts/smoke.sh http://localhost:8080` ✅
- Production smoke supports optional auth-flow via `KAKAO_TEST_ACCESS_TOKEN`

## API completion snapshot (2026-03-04)
- Budget API (`/api/settings/budget`)
  - implementation: DONE
  - swagger/openapi annotations: DONE
  - tests: DONE (`BudgetControllerWebMvcTest` happy + invalid payload)
- Home Summary API (`/api/home/summary`)
  - implementation: DONE
  - swagger/openapi annotations: DONE
  - tests: DONE (`HomeControllerWebMvcTest`)
- Transaction API (`/api/transactions`, `/tag`, `/exclude`)
  - implementation: DONE
  - swagger/openapi annotations: DONE
  - tests: DONE (`TransactionControllerWebMvcTest` list/tag/exclude + edge cases)
- Report API (`/api/reports/monthly-tags`, `/api/reports/daily-spending`)
  - implementation: DONE
  - swagger/openapi annotations: DONE
  - tests: DONE (`ReportControllerWebMvcTest` happy + required param edge cases)
