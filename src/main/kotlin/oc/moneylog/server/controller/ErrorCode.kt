package oc.moneylog.server.controller

enum class ErrorCode {
    BAD_REQUEST,
    VALIDATION_ERROR,
    MISSING_PARAMETER,
    MALFORMED_JSON,
    INTERNAL_ERROR,
}
