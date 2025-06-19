package com.example.schoolapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolapp.responses.AttendanceResponse
import com.example.schoolapp.responses.GetStudentLeaveRequestResponse

class LeaveRequestListViewModel: ViewModel() {
    private val _leaveRequestListData = MutableLiveData<GetStudentLeaveRequestResponse>()
    val leaveRequestListData: LiveData<GetStudentLeaveRequestResponse> get() = _leaveRequestListData
    fun setLeaveRequestListData(leaveRequestDetailViewModel: GetStudentLeaveRequestResponse) {
        _leaveRequestListData.value = leaveRequestDetailViewModel
    }
}