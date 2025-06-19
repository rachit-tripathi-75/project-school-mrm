package com.example.schoolapp.responses

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class AcademicPlanResponse(
    @SerializedName("Msg") val message: String,
    @SerializedName("type") val type: String,
    @SerializedName("status") val status: Int,
    @SerializedName("path") val path: String,
    @SerializedName("data") val data: List<AcademicPlan>
)

data class AcademicPlan(
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("from_date") val fromDate: String,
    @SerializedName("to_date") val toDate: String,
    @SerializedName("announced_later") val announcedLater: String,
    @SerializedName("status") val status: String,
    @SerializedName("created_at") val createdAt: String
) {

    fun getFormattedDateRange(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        return try {
            val fromDateFormatted = outputFormat.format(inputFormat.parse(fromDate) ?: Date())
            val toDateFormatted = outputFormat.format(inputFormat.parse(toDate) ?: Date())

            if (fromDate == toDate) {
                fromDateFormatted
            } else {
                "$fromDateFormatted - $toDateFormatted"
            }
        } catch (e: Exception) {
            "$fromDate - $toDate"
        }
    }

    fun getFormattedCreatedDate(): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

        return try {
            val date = inputFormat.parse(createdAt) ?: Date()
            "Created: ${outputFormat.format(date)}"
        } catch (e: Exception) {
            "Created: ${createdAt.split(" ")[0]}"
        }
    }

    fun isActive(): Boolean = status == "1"

    fun isAnnouncedLater(): Boolean = announcedLater == "1"
}