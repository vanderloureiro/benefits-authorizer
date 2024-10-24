package dev.vanderloureiro.benefits_authorizer.account

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "accounts")
data class Account(
    @Id
    @GeneratedValue
    var id: Long?,
    var name: String,
    var foodAmount: BigDecimal = BigDecimal.ZERO,
    var mealAmount: BigDecimal = BigDecimal.ZERO,
    var cashAmount: BigDecimal = BigDecimal.ZERO,
    var totalAmount: BigDecimal = BigDecimal.ZERO
)
