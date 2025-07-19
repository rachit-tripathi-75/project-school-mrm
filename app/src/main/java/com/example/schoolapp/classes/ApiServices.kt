package com.example.schoolapp.classes


import com.example.schoolapp.requests.PaymentHistoryRequest
import com.example.schoolapp.requests.UpdateFcmTokenRequest
import com.example.schoolapp.responses.AcademicPlanResponse
import com.example.schoolapp.responses.AttendanceResponse
import com.example.schoolapp.responses.FeeInstallmentDetailResponse
import com.example.schoolapp.responses.FeeInstallmentsResponse
import com.example.schoolapp.responses.GetExamDetailResponse
import com.example.schoolapp.responses.GetMarksResponse
import com.example.schoolapp.responses.GetStudentLeaveRequestResponse
import com.example.schoolapp.responses.HomeworkResponse
import com.example.schoolapp.responses.LoginResponse
import com.example.schoolapp.responses.NoticeDetailResponse
import com.example.schoolapp.responses.PaymentHistoryResponse
import com.example.schoolapp.responses.StudentDetailResponse
import com.example.schoolapp.responses.SubjectNotesResponse
import com.example.schoolapp.responses.SubmitLeaveRequestResponse
import com.example.schoolapp.responses.TimeTableResponse
import com.example.schoolapp.responses.UpdateFcmTokenResponse
import okhttp3.Cookie
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
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

    interface AttendanceApiService {
        @FormUrlEncoded
        @POST("API/get_attendance_by_student")
        fun getStudentAttendance(
            @Header("Authorization") authorization: String,
            @Header("Content-Type") contentType: String,
            @Header("Cookie") cookie: String,
            @Field("student_id") studentId: String,
            @Field("month") month: String
        ): Call<AttendanceResponse>
    }

    interface HomeworkApiService {
        @FormUrlEncoded
        @POST("API/getHomework")
        fun getHomework(
            @Header("Authorization") authorization: String,
            @Header("Content-Type") contentType: String,
            @Header("Cookie") cookie: String,
            @Field("section_id") sectionId: String
        ): Call<HomeworkResponse>
    }

    // original submit leave request API.......

//    interface SubmitLeaveRequestApiService {
//        @FormUrlEncoded
//        @POST("API/submitStudentLeaveRequest")
//        fun submitLeaveRequest(
//            @Header("Content-Type") contentType: String,
//            @Header("Cookie") cookie: String,
//            @Field("student_id") studentId: String,
//            @Field("LType") leaveType: String,
//            @Field("FDate") startDate: String,
//            @Field("TDate") endDate: String,
//            @Field("Reason") leaveReason: String
//        ): Call<SubmitLeaveRequestResponse>
//    }

    // new submit leave request API.............

    interface SubmitLeaveRequestApiService {
        @FormUrlEncoded
        @POST("API/submitStudentLeaveRequest")
        fun submitLeaveRequest(
            @Header("Content-Type") contentType: String,
            @Header("Cookie") cookie: String,
            @Field("student_id") studentId: String,
            @Field("LType") leaveType: String,
            @Field("FDate") startDate: String,
            @Field("TDate") endDate: String,
            @Field("Reason") leaveReason: String
        ): Call<SubmitLeaveRequestResponse>
    }

    interface GetStudentLeaveRequestApiService {
        @FormUrlEncoded
        @POST("API/getStudentLeaveRequest")
        fun getStudentLeaveRequests(
            @Header("Content-Type") contentType: String,
            @Header("Cookie") cookie: String,
            @Field("student_id") studentId: String
        ): Call<GetStudentLeaveRequestResponse>
    }

    interface SubjectNotesApiService {
        @FormUrlEncoded
        @POST("API/getSubjectNotes")
        fun getSubjectNotes(
            @Header("Authorization") authorization: String,
            @Header("Content-Type") contentType: String,
            @Header("Cookie") cookie: String,
            @Field("section_id") sectionId: String
        ): Call<SubjectNotesResponse>
    }

    interface AcademicPlanApiService {
        @POST("API/getAcademicPlan")
        fun getAcademicPlan(
            @Header("Authorization") authorization: String,
            @Header("Content-Type") contentType: String,
            @Header("Cookie") cookie: String
        ): Call<AcademicPlanResponse>
    }

    interface UpdateFCMTokenApiService {
        @PUT("API/updateFcmToken")
        fun updateFcmToken(
            @Header("Content-Type") contentType: String,
            @Header("Cookie") cookie: String,
            @Body updateFcmTokenRequest: UpdateFcmTokenRequest
        ) : Call<UpdateFcmTokenResponse>
    }


}