package com.example.schoolapp.classes

import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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

}