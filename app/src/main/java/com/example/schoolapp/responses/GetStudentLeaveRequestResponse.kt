package com.example.schoolapp.responses


import com.google.gson.annotations.SerializedName


data class GetStudentLeaveRequestResponse(
    @SerializedName("Msg")
    val msg: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val data: List<LeaveRequestData>
)

data class LeaveRequestData(
    @SerializedName("id")
    val id: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("LType")
    val lType: String,
    @SerializedName("FDate")
    val fDate: String,
    @SerializedName("TDate")
    val tDate: String,
    @SerializedName("Reason")
    val reason: String,
    @SerializedName("ApproveStatus")
    val approveStatus: String,
    @SerializedName("ApprovalStatusText")
    val approvalStatusText: String,
    @SerializedName("Created")
    val created: String
)
