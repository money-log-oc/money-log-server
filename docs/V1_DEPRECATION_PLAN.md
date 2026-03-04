# /api/v1 Deprecation Plan

## Current status
- App migrated to `/api/*` routes (app commit: 0d3abf0).
- Server still provides `/api/v1/*` compatibility routes.

## Removal checklist
- [x] 앱 `/api/*` 마이그레이션
- [ ] 서버 로그에서 `/api/v1/*` 호출 0건 확인 (최소 3일)
- [ ] 클라이언트 릴리즈 반영 확인
- [ ] `LegacyV1CompatController` 제거
- [ ] `/api/v1/*` 테스트 제거/정리

## Target
- Remove compatibility controller after migration stability window.
