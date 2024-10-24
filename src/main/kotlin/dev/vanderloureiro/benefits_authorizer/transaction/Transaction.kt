package dev.vanderloureiro.benefits_authorizer.transaction

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import java.math.BigDecimal
import java.time.ZonedDateTime

@Entity
@Table(name = "transactions")
class Transaction(
    @Id
    @GeneratedValue
    var id: Long?,
    val accountId: Long,
    val amount: BigDecimal,
    val mcc: String,
    val merchant: String,
    @CreationTimestamp
    var createdAt: ZonedDateTime?
) {
}