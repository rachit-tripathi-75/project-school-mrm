package com.example.schoolapp.responses

data class SubmitLeaveRequestResponse(
    val Msg: String,
    val type: String,
    val status: Int,
    val data: List<Any>
)