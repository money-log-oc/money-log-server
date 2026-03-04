# Security skeleton

- Public endpoints:
  - `/actuator/health`
  - `/swagger-ui.html`, `/swagger-ui/**`, `/v3/api-docs/**`
  - `/api/auth/**`, `/api/v1/auth/**`
- Protected: all others (authentication required)

Next step:
- Add JWT filter + token provider + role-based authorization.
