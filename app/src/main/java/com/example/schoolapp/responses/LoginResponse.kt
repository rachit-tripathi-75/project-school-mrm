package com.example.schoolapp.responses

data class LoginResponse(
    val Msg: String,
    val type: String,
    val status: Int,
    val `data`: Data
)

data class Data(
    val name: String,
    val email: String,
    val stu_id: String,
    val roll_no: String?, // Nullable String to handle potential null values
    val mobile: String,
    val school_id: String,
    val token: String
)