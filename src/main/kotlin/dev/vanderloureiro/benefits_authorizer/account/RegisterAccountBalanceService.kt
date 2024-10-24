package dev.vanderloureiro.benefits_authorizer.account

import dev.vanderloureiro.benefits_authorizer.category.Category
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class RegisterAccountBalanceService(val accountRepository: AccountRepository) {

    fun execute(account: Account, amount: BigDecimal, category: Category) {
        when (category) {
            Category.MEAL -> {
                saveMealBalance(amount, account)
            }
            Category.FOOD -> {
                saveFoodBalance(amount, account)
            }
            else -> {
                saveCashBalance(amount, account)
            }
        }
    }

    private fun saveMealBalance(amount: BigDecimal, account: Account) {
        if (amount >= account.mealAmount) {
            throw Exception()
        }
        account.mealAmount.minus(amount);
        account.totalAmount.minus(amount);
        accountRepository.save(account);
    }

    private fun saveFoodBalance(amount: BigDecimal, account: Account) {
        if (amount >= account.foodAmount) {
            throw Exception();
        }
        account.foodAmount.minus(amount);
        account.totalAmount.minus(amount);
        accountRepository.save(account);
        return;
    }

    private fun saveCashBalance(amount: BigDecimal, account: Account) {
        if (amount >= account.cashAmount) {
            throw Exception();
        }
        account.cashAmount.minus(amount);
        account.totalAmount.minus(amount);
        accountRepository.save(account);
    }
 }