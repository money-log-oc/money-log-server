package oc.moneylog.server.controller

import jakarta.servlet.http.HttpServletRequest
import oc.moneylog.server.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException, req: HttpServletRequest): ResponseEntity<ErrorResponse> =
        build(
            status = HttpStatus.BAD_REQUEST,
            code = ErrorCode.BAD_REQUEST,
            message = e.message ?: "bad request",
            req = req,
        )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException, req: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val msg = e.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "validation failed"
        return build(
            status = HttpStatus.BAD_REQUEST,
            code = ErrorCode.VALIDATION_ERROR,
            message = msg,
            req = req,
        )
    }

    @ExceptionHandler(MissingServletRequestParameterException::class)
    fun handleMissingParam(e: MissingServletRequestParameterException, req: HttpServletRequest): ResponseEntity<ErrorResponse> =
        build(
            status = HttpStatus.BAD_REQUEST,
            code = ErrorCode.MISSING_PARAMETER,
            message = "missing parameter: ${e.parameterName}",
            req = req,
        )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleMalformedJson(req: HttpServletRequest): ResponseEntity<ErrorResponse> =
        build(
            status = HttpStatus.BAD_REQUEST,
            code = ErrorCode.MALFORMED_JSON,
            message = "malformed json request",
            req = req,
        )

    @ExceptionHandler(Exception::class)
    fun handleUnknown(req: HttpServletRequest): ResponseEntity<ErrorResponse> =
        build(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            code = ErrorCode.INTERNAL_ERROR,
            message = "internal server error",
            req = req,
        )

    private fun build(
        status: HttpStatus,
        code: ErrorCode,
        message: String,
        req: HttpServletRequest,
    ): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(status).body(
            ErrorResponse(
                message = message,
                code = code.name,
                path = req.requestURI,
                timestamp = OffsetDateTime.now().toString(),
            ),
        )
    }
}
