package com.example.schoolapp.responses

data class GetMarksResponse(
    val Msg: String,
    val type: String,
    val status: Int,
    val data: List<StudentMarkData>
)

data class StudentMarkData(
    val sst_id: String,
    val stu_id: String,
    val scholar: String,
    val StudName: String,
    val exam_id: String,
    val ExamName: String,
    val mark: String,
    val sessionid: String
)
