package dev.vanderloureiro.benefits_authorizer.transaction.io

import java.math.BigDecimal

data class TransactionRequest(
    val account: Long,
    val amount: BigDecimal,
    val merchant: String,
    val mcc: String)
