package com.example.schoolapp.responses

import com.google.gson.annotations.SerializedName

data class TimeTableResponse(
    val Msg: String,
    val type: String,
    val status: Int,
    val data: List<TimeTableScheduleData>
)

data class TimeTableScheduleData(
    val day: String,
    @SerializedName("day_no") val dayNo: String,
    val period: String,
    @SerializedName("roomId") val roomId: String,
    @SerializedName("drag_id") val dragId: String,
    @SerializedName("t_p_id") val tPId: String,
    val email: String,
    val teachername: String,
    val subid: String,
    @SerializedName("sub_name") val subName: String,
    @SerializedName("sub_type") val subType: String,
    val room: String,
    @SerializedName("main_sec_name") val mainSecName: String,
    val section: String
)