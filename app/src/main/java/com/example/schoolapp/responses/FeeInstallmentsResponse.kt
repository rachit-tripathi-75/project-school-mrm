package com.example.schoolapp.responses

data class FeeInstallmentsResponse(
    val Msg: String,
    val type: String,
    val status: Int,
    val data: List<FeeInstallment>
)

data class FeeInstallment(
    val feeInstallment: String,
    val monthname: String
)