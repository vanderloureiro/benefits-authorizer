package dev.vanderloureiro.benefits_authorizer.category

import org.springframework.stereotype.Service

@Service
class ResolveCategoryService {

    fun execute(mcc: String, merchant: String? = null): Category {
        merchant?.let {
            if (it.contains("RESTAURANTE", true)) {
                return Category.FOOD
            }
            if (it.contains("MERCADO", true)) {
                return Category.MEAL
            }
        }
        return resolveByMcc(mcc);
    }

    private fun resolveByMcc(mcc: String): Category {
        return when(mcc) {
            "5411" -> Category.FOOD
            "5412" -> Category.FOOD
            "5811" -> Category.MEAL
            "5812" -> Category.MEAL
            else -> Category.CASH
        }
    }
}