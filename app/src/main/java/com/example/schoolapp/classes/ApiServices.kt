package com.example.schoolapp.classes


import com.example.schoolapp.requests.PaymentHistoryRequest
import com.example.schoolapp.responses.FeeInstallmentDetailResponse
import com.example.schoolapp.responses.FeeInstallmentsResponse
import com.example.schoolapp.responses.GetExamDetailResponse
import com.example.schoolapp.responses.GetMarksResponse
import com.example.schoolapp.responses.LoginResponse
import com.example.schoolapp.responses.NoticeDetailResponse
import com.example.schoolapp.responses.PaymentHistoryResponse
import com.example.schoolapp.responses.StudentDetailResponse
import com.example.schoolapp.responses.TimeTableResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Streaming

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

    interface GetNoticeDetailApiService {
        @POST("API/getNoticedetail")
        fun getNoticeDetail(
            @Header("Authorization") authorization: String,
            @Header("Cookie") cookie: String
        ): Call<NoticeDetailResponse>
    }

    interface DownloadPdfApiService {
        @Streaming
        @POST("assets/doc/{pdf_name}")
        fun downloadPdf(
            @Path("pdf_name") pdfName: String
        ): Call<ResponseBody>
    }

    interface FeeInstallmentsApiService {
        @FormUrlEncoded
        @POST("API/getFeeInstallments")
        fun getFeeInstallments(
            @Header("Authorization") authorization: String,
            @Header("Cookie") cookie: String,
            @Field("fee_id") feeId: String,
            @Field("sessionid") sessionId: String,
            @Field("stu_id") stuId: String
        ): Call<FeeInstallmentsResponse>
    }

    interface FeeInstallmentDetailsApiService {
        @FormUrlEncoded
        @POST("API/getFeeInstDetail")
        fun getFeeInstallmentDetails(
            @Header("Authorization") authorization: String,
            @Header("Cookie") cookie: String,
            @Field("sec_id") sectionid: String,
            @Field("inst") installment: String,
            @Field("fee_id") feeId: String,
            @Field("sessionid") sessionId: String
        ): Call<FeeInstallmentDetailResponse>
    }

    interface PaymentHistoryAPiService {
        @POST("API/getStudentLedger")
        fun getStudentPaymentHistory(
            @Header("Authorization") authorization: String,
            @Header("Content-Type") contentType: String,
            @Header("Cookie") cookie: String,
            @Body paymentHistoryModel: PaymentHistoryRequest
        ): Call<PaymentHistoryResponse>
    }

}