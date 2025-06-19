package com.example.schoolapp.responses

data class HomeworkResponse(
    val Msg: String,
    val type: String,
    val status: Int,
    val data: List<HomeworkData>
)

data class HomeworkData(
    val id: String,
    val subject: String,
    val description: String,
    val date: String
    // Add other fields based on the actual API response
)
