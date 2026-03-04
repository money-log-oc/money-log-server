# Release Checklist (v1)

## 0) Scope freeze
- [ ] Release branch/commit hash 확정
- [ ] 변경 범위(서버/앱) 최종 요약 작성

## 1) Server preflight
- [ ] `./gradlew test` 통과
- [ ] Flyway migration 확인 (`V1__init_schema.sql` 적용 상태)
- [ ] Swagger 노출 확인
  - [ ] `/swagger-ui.html`
  - [ ] `/v3/api-docs`
- [ ] 보안 설정 확인
  - [ ] 보호 API `hasRole("USER")`
  - [ ] refresh 토큰으로 보호 API 접근 차단

## 2) Railway deploy
- [ ] 환경변수 확인
  - [ ] `SPRING_PROFILES_ACTIVE=prod`
  - [ ] `SPRING_DATASOURCE_URL`
  - [ ] `SPRING_DATASOURCE_USERNAME`
  - [ ] `SPRING_DATASOURCE_PASSWORD`
  - [ ] `JWT_SECRET`
  - [ ] `JWT_ACCESS_EXP_SECONDS`
  - [ ] `JWT_REFRESH_EXP_SECONDS`
- [ ] 배포 트리거
- [ ] 최신 커밋 반영 확인

## 3) Smoke test
- [ ] `./scripts/smoke.sh https://money-log-server-production.up.railway.app`
- [ ] (선택) `KAKAO_TEST_ACCESS_TOKEN=<token> ./scripts/smoke.sh`
- [ ] `/actuator/health` = `UP`

## 4) App preflight
- [ ] `flutter analyze` 통과
- [ ] 로그인(카카오) → 서버 auth 성공
- [ ] 자동 로그인(secure storage) 동작
- [ ] 홈/내역/리포트/설정 API 동작 확인

## 5) Manual QA
- [ ] 월급일/예산 수정 후 홈 반영 확인
- [ ] 내역 태그/제외/해제 플로우 확인
- [ ] 리포트 빈상태/에러상태/실데이터 확인
- [ ] 만료 access 토큰 시 refresh 재발급 동작 확인

## 6) Rollback plan
- [ ] 직전 안정 배포 commit/hash 기록
- [ ] Railway 롤백 절차 링크/담당자 확인
- [ ] 긴급 장애시 legacy `/api/v1/*` 토글 전략 확인

## 7) Release sign-off
- [ ] 배포 완료 시간 기록
- [ ] 변경점 요약 공유
- [ ] Known issues 업데이트
