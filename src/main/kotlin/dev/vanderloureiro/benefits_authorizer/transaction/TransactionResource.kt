package dev.vanderloureiro.benefits_authorizer.transaction

import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionRequest
import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionResponse
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

    @PostMapping("/v1/transactions")
    fun executeV1(@RequestBody @Valid request: TransactionRequest): ResponseEntity<TransactionResponse> {

        return ResponseEntity.ok(registerTransactionService.execute(request))
    }

    @PostMapping("/v2/transactions")
    fun executeV2(@RequestBody @Valid request: TransactionRequest): ResponseEntity<TransactionResponse> {

        return ResponseEntity.ok(registerTransactionFallbackService.execute(request))
    }

    @PostMapping("/v3/transactions")
    fun executeV3(@RequestBody @Valid request: TransactionRequest): ResponseEntity<TransactionResponse> {

        return ResponseEntity.ok(registerTransactionMerchantService.execute(request))
    }
}