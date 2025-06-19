package com.example.schoolapp.models

import java.io.Serializable
import java.util.Calendar


data class LeaveRequestModel(
    val id: String,
    val reason: String,
    val leaveType: String,
    val startDate: String,
    val endDate: String,
    val description: String,
    val approveStatus: String,
    val createdAt: String
) : Serializable
