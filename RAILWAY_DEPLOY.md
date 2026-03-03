# Railway Deploy Playbook

## 1) Service linkage
- Project: money-log-server
- Source repo: `money-log-oc/money-log-server`
- Branch: `main`

## 2) Variables
- `SPRING_PROFILES_ACTIVE=prod`
- `SPRING_DATASOURCE_URL=jdbc:postgresql://...`
- `SPRING_DATASOURCE_USERNAME=...`
- `SPRING_DATASOURCE_PASSWORD=...`
- `PORT=8080`

## 3) Verify deploy revision
After deploy, call:
- `/api/v1/version`
- `/api/v1/auth/kakao` (POST)

If still 404 on new endpoints:
1. Railway dashboard → service → Deployments
2. Click latest commit hash and `Redeploy`
3. Confirm build log contains latest commit id

## 4) Health
- `/actuator/health` must return `UP`
