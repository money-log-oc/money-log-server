package oc.moneylog.server.adapter.`in`.web.transaction

import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import oc.moneylog.server.dto.ExcludeUpdateRequest
import oc.moneylog.server.dto.TagUpdateRequest
import oc.moneylog.server.dto.TransactionResponse
import oc.moneylog.server.service.TransactionService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/transactions")
class TransactionController(
    private val transactionService: TransactionService,
) {
    @Operation(summary = "거래 목록 조회")
    @GetMapping
    fun listTransactions(
        @RequestParam(required = false) month: String?,
        @RequestParam(defaultValue = "false") unclassified: Boolean,
    ): List<TransactionResponse> =
        transactionService.list(month, unclassified).map { tx ->
            TransactionResponse(tx.id, tx.occurredAt.toString(), tx.merchant, tx.amount, tx.tags.toList(), tx.excluded, tx.exclusionReason)
        }

    @Operation(summary = "거래 태그 수정")
    @PatchMapping("/{id}/tag")
    fun updateTags(@PathVariable id: Long, @Valid @RequestBody req: TagUpdateRequest): TransactionResponse {
        val tx = transactionService.updateTags(id, req.tagIds)
        return TransactionResponse(tx.id, tx.occurredAt.toString(), tx.merchant, tx.amount, tx.tags.toList(), tx.excluded, tx.exclusionReason)
    }

    @Operation(summary = "거래 제외 처리")
    @PatchMapping("/{id}/exclude")
    fun updateExclude(@PathVariable id: Long, @RequestBody req: ExcludeUpdateRequest): TransactionResponse {
        val tx = transactionService.updateExcluded(id, req.excluded, req.reason)
        return TransactionResponse(tx.id, tx.occurredAt.toString(), tx.merchant, tx.amount, tx.tags.toList(), tx.excluded, tx.exclusionReason)
    }
}
