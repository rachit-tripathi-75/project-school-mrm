package com.example.schoolapp.classes

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiClient {
    const val BASE_URL = "https://erp.apschitrakoot.in/"

    val loginInstance: ApiServices.LoginApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.LoginApiService::class.java)
    }

    val getMarksInstance: ApiServices.GetMarksApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.GetMarksApiService::class.java)
    }

    val getExamDetailInstance: ApiServices.GetExamDetailApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.GetExamDetailApiService::class.java)
    }

    val getStudentDetailInstance: ApiServices.GetStudentDetailApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.GetStudentDetailApiService::class.java)
    }

    val getTimeTableInstance: ApiServices.GetTimeTableApiServices by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.GetTimeTableApiServices::class.java)
    }

    val getNoticeDetailInstance: ApiServices.GetNoticeDetailApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.GetNoticeDetailApiService::class.java)
    }

    val downloadPdfInstance: ApiServices.DownloadPdfApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.DownloadPdfApiService::class.java)
    }

    val feeInstallmentInstance: ApiServices.FeeInstallmentsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.FeeInstallmentsApiService::class.java)
    }

    val feeInstallmentDetailInstance: ApiServices.FeeInstallmentDetailsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.FeeInstallmentDetailsApiService::class.java)
    }

    val paymentHistoryInstance: ApiServices.PaymentHistoryAPiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.PaymentHistoryAPiService::class.java)
    }

    val attendanceInstance: ApiServices.AttendanceApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.AttendanceApiService::class.java)
    }

    val homeworkInstance: ApiServices.HomeworkApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.HomeworkApiService::class.java)
    }

    val submitLeaveRequestInstance: ApiServices.SubmitLeaveRequestApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.SubmitLeaveRequestApiService::class.java)
    }

    val leaveRequestListInstance: ApiServices.GetStudentLeaveRequestApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.GetStudentLeaveRequestApiService::class.java)
    }

    val subjectNotesInstance: ApiServices.SubjectNotesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.SubjectNotesApiService::class.java)
    }

    val academicPlanInstance: ApiServices.AcademicPlanApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.AcademicPlanApiService::class.java)
    }

    val updateFcmTokenInstance: ApiServices.UpdateFCMTokenApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices.UpdateFCMTokenApiService::class.java)
    }

}