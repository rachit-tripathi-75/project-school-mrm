package com.example.schoolapp.classes

import com.example.schoolapp.responses.GetExamDetailResponse
import com.example.schoolapp.responses.GetMarksResponse
import com.example.schoolapp.responses.LoginResponse
import com.example.schoolapp.responses.StudentDetailResponse
import com.example.schoolapp.responses.TimeTableResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

class ApiServices {
    interface LoginApiService {
        @FormUrlEncoded
        @POST("API/login")
        fun login(
            @Field("enrollment") enrollment: String,
            @Field("password") password: String
        ): Call<LoginResponse>
    }
    interface GetMarksApiService {
        @FormUrlEncoded
        @POST("API/getMarks")
        fun getMarks(
            @Header("Authorization") authorization: String,
            @Header("Cookie") cookie: String,
            @Field("scholar") enrollmentNumber: String,
            @Field("examid") examId: String
        ): Call<GetMarksResponse>
    }

    interface GetExamDetailApiService {
        @POST("API/getExamdetail")
        fun getExamDetail(
            @Header("Authorization") authorization: String,
            @Header("Cookie") cookie: String
        ): Call<GetExamDetailResponse>
    }

    interface GetStudentDetailApiService {
        @FormUrlEncoded
        @POST("API/getStudentdetail")
        fun getStudentDetail(
            @Header("Authorization") authorization: String,
            @Header("Cookie") cookie: String,
            @Field("enrollment") enrollment: String
        ): Call<StudentDetailResponse>
    }

    interface GetTimeTableApiServices {
        @FormUrlEncoded
        @POST("API/getStudenttimetable")
        fun getStudentTimeTable(
            @Header("Authorization") authorization: String,
            @Header("Cookie") cookie: String,
            @Field("sectionid") sectionId: String,
            @Field("dayno") dayNumber: String
        ): Call<TimeTableResponse>
    }

}