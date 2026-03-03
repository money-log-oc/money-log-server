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
