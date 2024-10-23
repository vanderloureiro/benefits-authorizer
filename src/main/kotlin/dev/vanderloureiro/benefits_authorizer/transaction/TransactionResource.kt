package dev.vanderloureiro.benefits_authorizer.transaction

import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionRequest
import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/transactions")
class TransactionResource(val service: RegisterTransactionService) {

    @PostMapping
    fun execute(@RequestBody @Valid request: TransactionRequest): ResponseEntity<TransactionResponse> {

        return ResponseEntity.ok(service.execute(request))
    }
}