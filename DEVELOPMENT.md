# Moneylog Server – Dev Notes

## Base URL
- Production: `https://money-log-server-production.up.railway.app`

## Implemented API (v1)
- `GET /api/v1/home/summary`
- `GET /api/v1/settings/budget`
- `PUT /api/v1/settings/budget`
- `GET /api/v1/transactions?month=YYYY-MM&unclassified=true|false`
- `PATCH /api/v1/transactions/{id}/tag`
- `PATCH /api/v1/transactions/{id}/exclude`
- `GET /api/v1/reports/monthly-tags?month=YYYY-MM`
- `GET /api/v1/reports/daily-spending?month=YYYY-MM`
- `POST /api/v1/auth/kakao` (skeleton)

## Deploy check
1. `/actuator/health` should be `UP`
2. `/api/v1/version` should return `v1-alpha` after latest deploy

## Known issue
- Railway may keep old image when auto deploy hook is delayed.
- In that case trigger manual redeploy from Railway dashboard.
