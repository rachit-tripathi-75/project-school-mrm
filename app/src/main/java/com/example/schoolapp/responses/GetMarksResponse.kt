package com.example.schoolapp.responses

import com.google.gson.annotations.SerializedName

data class GetMarksResponse(
    @SerializedName("Msg") val msg: String,
    val type: String,
    val status: Int,
    val data: MarksData
)

data class MarksData(
    @SerializedName("student_name") val studentName: String,
    val scholar: String,
    val marks: List<SubjectMark>,
    @SerializedName("Totalmm") val totalMaxMarks: Int,
    @SerializedName("Totalob") val totalObtainedMarks: Int
)

data class SubjectMark(
    val marks: String,
    val subject: String?,
    @SerializedName("MM") val maxMarks: String
)