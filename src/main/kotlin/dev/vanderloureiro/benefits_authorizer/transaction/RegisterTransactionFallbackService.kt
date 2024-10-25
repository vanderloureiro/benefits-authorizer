package dev.vanderloureiro.benefits_authorizer.transaction

import dev.vanderloureiro.benefits_authorizer.account.AccountRepository
import dev.vanderloureiro.benefits_authorizer.account.NoBenefitBalanceException
import dev.vanderloureiro.benefits_authorizer.account.NoCashBalanceException
import dev.vanderloureiro.benefits_authorizer.account.RegisterAccountBalanceService
import dev.vanderloureiro.benefits_authorizer.category.Category
import dev.vanderloureiro.benefits_authorizer.category.ResolveCategoryService
import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionRequest
import dev.vanderloureiro.benefits_authorizer.transaction.io.TransactionResponse
import org.springframework.stereotype.Service

@Service
class RegisterTransactionFallbackService (
    val resolveCategoryService: ResolveCategoryService,
    val accountRepository: AccountRepository,
    val transactionRepository: TransactionRepository,
    val registerAccountBalanceService: RegisterAccountBalanceService
) {

    private val unknownErrorStatusCode: String = "07";
    private val balanceErrorStatusCode: String = "51";
    private val successStatusCode: String = "00";

    fun execute(request: TransactionRequest) : TransactionResponse {

        val accountOpt = accountRepository.findById(request.account);
        if (accountOpt.isEmpty) {
            return TransactionResponse(unknownErrorStatusCode)
        }

        val category = resolveCategoryService.execute(request.mcc);

        try {
            registerAccountBalanceService.execute(accountOpt.get(), request.amount, category)
        } catch (e: NoBenefitBalanceException) {
            try {
                registerAccountBalanceService.execute(accountOpt.get(), request.amount, Category.CASH)
            } catch (e: NoCashBalanceException) {
                return TransactionResponse(balanceErrorStatusCode)
            }
        } catch (e: NoCashBalanceException) {
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