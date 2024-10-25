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
        if (account.mealAmount.compareTo(amount) <= 0) {
            throw NoBenefitBalanceException()
        }
        account.mealAmount = account.mealAmount.subtract(amount);
        account.totalAmount = account.totalAmount.subtract(amount);
        accountRepository.save(account);
    }

    private fun saveFoodBalance(amount: BigDecimal, account: Account) {
        if (account.foodAmount.compareTo(amount) <= 0) {
            throw NoBenefitBalanceException();
        }
        account.foodAmount = account.foodAmount.subtract(amount);
        account.totalAmount = account.totalAmount.subtract(amount);
        accountRepository.save(account);
        return;
    }

    private fun saveCashBalance(amount: BigDecimal, account: Account) {
        if (account.cashAmount.compareTo(amount) <= 0) {
            throw NoCashBalanceException();
        }
        account.cashAmount = account.cashAmount.subtract(amount);
        account.totalAmount = account.totalAmount.subtract(amount);
        accountRepository.save(account);
    }
 }