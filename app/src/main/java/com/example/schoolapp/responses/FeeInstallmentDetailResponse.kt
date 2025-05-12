package com.example.schoolapp.responses

import com.google.gson.annotations.SerializedName

data class FeeInstallmentDetailResponse(
    @SerializedName("Msg") val message: String,
    val type: String,
    val status: Int,
    val data: List<FeeInstallmentDetail>
)

data class FeeInstallmentDetail(
    val id: String,
    val standard: String,
    val section: String,
    @SerializedName("FeePolicyId") val feePolicyId: String,
    @SerializedName("feeType") val feeType: String,
    @SerializedName("feehead_id") val feeHeadId: String,
    @SerializedName("feeInstallment") val feeInstallment: String,
    val amount: String,
    @SerializedName("monthname") val monthName: String,
    @SerializedName("feeSubmissionStartDate") val startDate: String,
    @SerializedName("feeSubmissionEndDate") val endDate: String,
    @SerializedName("academicYear") val academicYear: String,
    @SerializedName("schId") val schoolId: String,
    val created: String,
    @SerializedName("createdBy") val createdBy: String,
    @SerializedName("createdIp") val createdIp: String
)