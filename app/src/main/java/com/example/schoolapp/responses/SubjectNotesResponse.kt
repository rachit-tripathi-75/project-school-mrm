package com.example.schoolapp.responses

import com.google.gson.annotations.SerializedName

data class SubjectNotesResponse(
    @SerializedName("Msg") val msg: String,
    @SerializedName("type") val type: String,
    @SerializedName("status") val status: Int,
    @SerializedName("path") val path: String,
    @SerializedName("data") val data: List<SubjectNote>
)

data class SubjectNote(
    @SerializedName("id") val id: String,
    @SerializedName("sst_id") val sstId: String,
    @SerializedName("sec_id") val secId: String,
    @SerializedName("sub_id") val subId: String,
    @SerializedName("subject") val subject: String,
    @SerializedName("file_note") val fileNote: String,
    @SerializedName("sessionid") val sessionId: String,
    @SerializedName("created_by") val createdBy: String,
    @SerializedName("created_on") val createdOn: String,
    @SerializedName("created_ip") val createdIp: String,
    @SerializedName("status") val status: String,
    @SerializedName("sub_name") val subName: String
)