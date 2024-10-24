package dev.vanderloureiro.benefits_authorizer.transaction

import dev.vanderloureiro.benefits_authorizer.account.AccountRepository
import dev.vanderloureiro.benefits_authorizer.account.RegisterAccountBalanceService
import dev.vanderloureiro.benefits_authorizer.category.ResolveCategoryService
import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionRequest
import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionResponse
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class RegisterTransactionService(
    val resolveCategoryService: ResolveCategoryService,
    val accountRepository: AccountRepository,
    val transactionRepository: TransactionRepository,
    val registerAccountBalanceService: RegisterAccountBalanceService) {

    private val unknownErrorStatusCode: String = "07";
    private val balanceErrorStatusCode: String = "51";
    private val successStatusCode: String = "00";

    @Transactional
    fun execute(request: TransactionRequest) : TransactionResponse {

        val accountOpt = accountRepository.findById(request.account);
        if (accountOpt.isEmpty) {
            return TransactionResponse(unknownErrorStatusCode)
        }

        val category = resolveCategoryService.execute(request.mcc, request.merchant);

        try {
            registerAccountBalanceService.execute(accountOpt.get(), request.amount, category)
        } catch (e: Exception) {
            return TransactionResponse(balanceErrorStatusCode);
        }
        
        registerBalance(request)
        return TransactionResponse(successStatusCode);
    }

    private fun registerBalance(request: TransactionRequest) {
        val transactionToSave = Transaction(
            id = null,
            accountId = request.account,
            amount = request.amount,
            mcc = request.mcc,
            merchant = request.merchant,
            createdAt = null);
        transactionRepository.save(transactionToSave);
    }


}