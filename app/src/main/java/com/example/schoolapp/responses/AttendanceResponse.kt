package com.example.schoolapp.responses

import com.google.gson.annotations.SerializedName

data class AttendanceResponse(
    @SerializedName("Msg")
    val message: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("status")
    val status: Int,
    @SerializedName("data")
    val data: List<AttendanceRecord>
)

data class AttendanceRecord(
    @SerializedName("id")
    val id: String,
    @SerializedName("student_id")
    val studentId: String,
    @SerializedName("student_name")
    val studentName: String,
    @SerializedName("section")
    val section: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("attendance")
    val attendance: String,
    @SerializedName("SessionId")
    val sessionId: String,
    @SerializedName("created_by")
    val createdBy: String,
    @SerializedName("created_at")
    val createdAt: String
)
