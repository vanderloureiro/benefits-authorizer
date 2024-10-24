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
    val name: String,
    val foodAmount: BigDecimal = BigDecimal.ZERO,
    val mealAmount: BigDecimal = BigDecimal.ZERO,
    val cashAmount: BigDecimal = BigDecimal.ZERO,
    val totalAmount: BigDecimal = BigDecimal.ZERO
)
