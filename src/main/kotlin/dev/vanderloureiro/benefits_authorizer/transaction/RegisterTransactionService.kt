package dev.vanderloureiro.benefits_authorizer.transaction

import dev.vanderloureiro.benefits_authorizer.account.AccountRepository
import dev.vanderloureiro.benefits_authorizer.category.ResolveCategoryService
import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionRequest
import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionResponse
import org.springframework.stereotype.Service

@Service
class RegisterTransactionService(
    val resolveCategoryService: ResolveCategoryService,
    val accountRepository: AccountRepository) {

    fun execute(request: TransactionRequest) : TransactionResponse {

        val accountOpt = accountRepository.findById(request.account);
        if (accountOpt.isEmpty) {
            return TransactionResponse("07")
        }

        val category = resolveCategoryService.execute(request.mcc, request.merchant);

        return TransactionResponse("00");
    }
}