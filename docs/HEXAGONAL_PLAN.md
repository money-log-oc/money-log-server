# Hexagonal Architecture Plan (money-log-server)

## Target package layout
- `oc.moneylog.server.domain`
  - entity/value object, domain rule
- `oc.moneylog.server.application`
  - usecase/service, port(in/out)
- `oc.moneylog.server.adapter.in.web`
  - REST controller, request/response mapping
- `oc.moneylog.server.adapter.out.persistence`
  - JPA entity/repository adapters
- `oc.moneylog.server.infrastructure`
  - config/security/openapi

## Migration steps
1. 도메인 모델 고정
2. UseCase 인터페이스/구현 분리
3. Controller -> UseCase 의존으로 전환
4. Persistence adapter 분리
5. 테스트를 유스케이스/어댑터 단위로 재정렬

## Definition of done
- Controller가 domain/persistence를 직접 참조하지 않음
- application port를 통해서만 데이터 접근
- 테스트가 계층별로 분리됨 (unit / integration)
