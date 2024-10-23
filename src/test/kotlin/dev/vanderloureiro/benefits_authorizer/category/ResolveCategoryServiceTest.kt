package dev.vanderloureiro.benefits_authorizer.category

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ResolveCategoryServiceTest {

    private val resolveCategoryService = ResolveCategoryService()

    @Test
    fun whenMealMccAndNullMerchant_returnMeal() {
        val result = resolveCategoryService.execute("5812");
        assertEquals(Category.MEAL, result);
    }

    @Test
    fun whenFoodMccAndNullMerchant_returnFood() {
        val result = resolveCategoryService.execute("5412");
        assertEquals(Category.FOOD, result);
    }

    @Test
    fun whenMealMccAndFoodMerchant_returnFood() {
        val result = resolveCategoryService.execute("5812", "Restaurante e Taqueria Por Dios");
        assertEquals(Category.FOOD, result);
    }

    @Test
    fun whenFoodMccAndMealMerchant_returnMeal() {
        val result = resolveCategoryService.execute("5412", "Mercado Dois Irmãos");
        assertEquals(Category.MEAL, result);
    }

    @Test
    fun whenNotMappedMccAndMerchant_returnCash() {
        val result = resolveCategoryService.execute("10");
        assertEquals(Category.CASH, result);
    }
}