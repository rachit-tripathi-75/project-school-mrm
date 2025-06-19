package com.example.schoolapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolapp.responses.AttendanceResponse
import com.example.schoolapp.responses.HomeworkResponse

class HomeworkViewModel: ViewModel() {
    private val _homeworkData = MutableLiveData<HomeworkResponse>()
    val homeworkData: LiveData<HomeworkResponse> get() = _homeworkData
    fun setHomeworkData(homeworkViewModel: HomeworkResponse) {
        _homeworkData.value = homeworkViewModel
    }

}
