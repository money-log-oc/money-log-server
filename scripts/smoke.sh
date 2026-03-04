#!/usr/bin/env bash
set -euo pipefail

BASE_URL="${1:-https://money-log-server-production.up.railway.app}"
KAKAO_TEST_ACCESS_TOKEN="${KAKAO_TEST_ACCESS_TOKEN:-}"

req_code() {
  local method="$1"
  local url="$2"
  local data="${3:-}"
  if [[ -n "$data" ]]; then
    curl -sS -o /tmp/moneylog_smoke_body.json -w "%{http_code}" -X "$method" "$url" \
      -H 'Content-Type: application/json' \
      -d "$data"
  else
    curl -sS -o /tmp/moneylog_smoke_body.json -w "%{http_code}" -X "$method" "$url"
  fi
}

echo "[1] health"
code=$(req_code GET "$BASE_URL/actuator/health")
[[ "$code" == "200" ]] || { echo "health failed: $code"; cat /tmp/moneylog_smoke_body.json; exit 1; }
python3 - <<'PY'
import json
body=json.load(open('/tmp/moneylog_smoke_body.json'))
assert body.get('status')=='UP', body
print('ok')
PY

echo "[2] swagger"
code=$(curl -sS -o /dev/null -w "%{http_code}" "$BASE_URL/swagger-ui.html")
[[ "$code" == "200" || "$code" == "302" ]] || { echo "swagger failed: $code"; exit 1; }
echo "ok ($code)"

echo "[3] auth negative (invalid kakao token -> 400 BAD_REQUEST)"
code=$(req_code POST "$BASE_URL/api/auth/kakao" '{"accessToken":"invalid-token"}')
[[ "$code" == "400" ]] || { echo "auth negative failed: $code"; cat /tmp/moneylog_smoke_body.json; exit 1; }
python3 - <<'PY'
import json
body=json.load(open('/tmp/moneylog_smoke_body.json'))
assert body.get('code') in ('BAD_REQUEST','VALIDATION_ERROR'), body
print('ok')
PY

if [[ -n "$KAKAO_TEST_ACCESS_TOKEN" ]]; then
  echo "[4] auth positive + protected API"
  code=$(req_code POST "$BASE_URL/api/auth/kakao" "{\"accessToken\":\"$KAKAO_TEST_ACCESS_TOKEN\"}")
  [[ "$code" == "200" ]] || { echo "auth positive failed: $code"; cat /tmp/moneylog_smoke_body.json; exit 1; }
  ACCESS_TOKEN=$(python3 - <<'PY'
import json
body=json.load(open('/tmp/moneylog_smoke_body.json'))
print(body.get('accessToken',''))
PY
)
  [[ -n "$ACCESS_TOKEN" ]] || { echo "missing access token"; exit 1; }

  code=$(curl -sS -o /tmp/moneylog_smoke_body.json -w "%{http_code}" "$BASE_URL/api/home/summary" \
    -H "Authorization: Bearer $ACCESS_TOKEN")
  [[ "$code" == "200" ]] || { echo "protected api failed: $code"; cat /tmp/moneylog_smoke_body.json; exit 1; }
  echo "ok"
else
  echo "[4] skip positive auth flow (set KAKAO_TEST_ACCESS_TOKEN to enable)"
fi

echo "smoke done"
