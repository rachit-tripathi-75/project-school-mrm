package com.example.schoolapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolapp.responses.TimeTableResponse

class TimeTableViewModel : ViewModel() {
    private val _timeTableData = MutableLiveData<TimeTableResponse>()
    val timeTableData: LiveData<TimeTableResponse> get() = _timeTableData

    fun setTimeTableData(timetableModel: TimeTableResponse) {
        _timeTableData.value = timetableModel
    }
}