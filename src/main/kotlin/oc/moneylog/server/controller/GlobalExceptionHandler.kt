package oc.moneylog.server.controller

import jakarta.servlet.http.HttpServletRequest
import oc.moneylog.server.dto.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.OffsetDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgument(e: IllegalArgumentException, req: HttpServletRequest): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                message = e.message ?: "bad request",
                code = "BAD_REQUEST",
                path = req.requestURI,
                timestamp = OffsetDateTime.now().toString(),
            ),
        )
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(e: MethodArgumentNotValidException, req: HttpServletRequest): ResponseEntity<ErrorResponse> {
        val msg = e.bindingResult.fieldErrors.firstOrNull()?.defaultMessage ?: "validation failed"
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            ErrorResponse(
                message = msg,
                code = "VALIDATION_ERROR",
                path = req.requestURI,
                timestamp = OffsetDateTime.now().toString(),
            ),
        )
    }
}
