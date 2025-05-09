package com.example.schoolapp.responses

data class GetExamDetailResponse(
    val Msg: String,
    val type: String,
    val status: Int,
    val data: List<ExamData>
)

data class ExamData(
    val id: String,
    val name: String,
    val max_mark: String,
    val created_by: String,
    val created_on: String,
    val status: String,
    val sessionid: String
)
