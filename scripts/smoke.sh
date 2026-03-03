#!/usr/bin/env bash
set -euo pipefail
BASE_URL="${1:-https://money-log-server-production.up.railway.app}"

echo "[1] health"
curl -fsS "$BASE_URL/actuator/health" >/dev/null && echo "ok"

echo "[2] home summary"
curl -fsS "$BASE_URL/api/v1/home/summary" | head -c 120; echo

echo "[3] transactions"
curl -fsS "$BASE_URL/api/v1/transactions?month=2026-03" | head -c 120; echo

echo "[4] report"
curl -fsS "$BASE_URL/api/v1/reports/monthly-tags?month=2026-03" | head -c 120; echo

echo "[5] auth skeleton"
curl -fsS -X POST "$BASE_URL/api/v1/auth/kakao" \
  -H 'Content-Type: application/json' \
  -d '{"accessToken":"dev-token-abcdef"}' | head -c 120; echo

echo "smoke done"
