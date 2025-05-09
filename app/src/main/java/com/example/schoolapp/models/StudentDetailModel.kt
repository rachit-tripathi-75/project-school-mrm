package com.example.schoolapp.models

import com.google.gson.annotations.SerializedName

data class StudentDetailModel(
    @SerializedName("sec_id") val sectionId: String,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("sectionname") val sectionName: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("enrollment") val enrollment: String,
    @SerializedName("roll_no") val rollNumber: String? = null,
    @SerializedName("dob") val dateOfBirth: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("father") val fatherName: String,
    @SerializedName("mother") val motherName: String,
    @SerializedName("adhaar") val aadhaarNumber: String? = null,
    @SerializedName("address") val address: String,
    @SerializedName("AcademicYear") val academicYear: String,
    @SerializedName("sessionid") val sessionId: String,
    @SerializedName("class_id") val classId: String,
    @SerializedName("standard") val standard: String,
    @SerializedName("stu_id") val studentId: String,
    @SerializedName("sidinc") val sidInc: String,
    @SerializedName("IsLeft") val isLeft: String,
    @SerializedName("father_no") val fatherMobile: String,
    @SerializedName("stu_img") val studentImage: String,
    @SerializedName("IsNew") val isNew: String
)