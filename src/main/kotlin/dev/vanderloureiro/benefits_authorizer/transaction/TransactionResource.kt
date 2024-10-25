package dev.vanderloureiro.benefits_authorizer.transaction

import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionRequest
import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionResponse
import io.swagger.v3.oas.annotations.Operation
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class TransactionResource(
    val registerTransactionService: RegisterTransactionService,
    val registerTransactionFallbackService: RegisterTransactionFallbackService,
    val registerTransactionMerchantService: RegisterTransactionMerchantService) {

    @Operation(summary = "Simpler version of the transaction registration process (L1)")
    @PostMapping("/v1/transactions")
    fun executeV1(@RequestBody @Valid request: TransactionRequest): ResponseEntity<TransactionResponse> {

        return ResponseEntity.ok(registerTransactionService.execute(request))
    }

    @Operation(summary = "Transaction registration process with fallback (L2)")
    @PostMapping("/v2/transactions")
    fun executeV2(@RequestBody @Valid request: TransactionRequest): ResponseEntity<TransactionResponse> {

        return ResponseEntity.ok(registerTransactionFallbackService.execute(request))
    }

    @Operation(summary = "Transaction registration process with fallback and merchant verification (L3)")
    @PostMapping("/v3/transactions")
    fun executeV3(@RequestBody @Valid request: TransactionRequest): ResponseEntity<TransactionResponse> {

        return ResponseEntity.ok(registerTransactionMerchantService.execute(request))
    }
}