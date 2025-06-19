package com.example.schoolapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolapp.responses.AttendanceResponse
import com.example.schoolapp.responses.PaymentHistoryResponse

class AttendanceViewModel: ViewModel() {
    private val _attendanceData = MutableLiveData<AttendanceResponse>()
    val attendanceData: LiveData<AttendanceResponse> get() = _attendanceData
    fun setAttendanceData(attendanceDetailViewModel: AttendanceResponse) {
        _attendanceData.value = attendanceDetailViewModel
    }
}