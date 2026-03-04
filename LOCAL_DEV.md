# Local development

## 1) Start local PostgreSQL with Docker
```bash
docker compose up -d
```

## 2) Run server with local profile
```bash
./gradlew bootRun --args='--spring.profiles.active=local'
```

## 3) Open Swagger
- http://localhost:8080/swagger-ui.html

## 4) Health check
- http://localhost:8080/actuator/health

## Verification log (2026-03-04)
- `docker compose up -d` ✅ (`moneylog-postgres` healthy)
- `./gradlew bootRun --args='--spring.profiles.active=local'` ✅ (8080)
- `GET /actuator/health` ✅ `UP`
- `GET /swagger-ui.html` ✅ (302 redirect)
