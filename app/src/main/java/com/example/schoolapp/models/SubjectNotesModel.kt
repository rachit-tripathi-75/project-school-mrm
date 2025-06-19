package com.example.schoolapp.models

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class SubjectNotesModel(
    val id: String,
    val sstId: String,
    val secId: String,
    val subId: String,
    val subject: String,
    val fileNote: String,
    val sessionId: String,
    val title: String,
    val description: String,
    val dueDate: String
)
