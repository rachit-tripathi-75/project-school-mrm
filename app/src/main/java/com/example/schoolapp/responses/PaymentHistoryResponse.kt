package com.example.schoolapp.responses

data class PaymentHistoryResponse(
    val Msg: String,
    val type: String,
    val status: Int,
    val data: List<TransactionData>
)

data class TransactionData(
    val transactionDate: String,
    val feeType: String,
    val installment: String,
    val amount: String,
    val type: String,
    val receipt: String?,
    val payment_mode: String?,
    val remark: String?
)