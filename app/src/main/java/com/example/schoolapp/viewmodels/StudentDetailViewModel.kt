package com.example.schoolapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolapp.models.StudentDetailModel
import com.example.schoolapp.responses.StudentDetailResponse

class StudentDetailViewModel : ViewModel() {
    private val _studentData = MutableLiveData<StudentDetailResponse>()
    val studentData: LiveData<StudentDetailResponse> get() = _studentData

    fun setStudentData(studentDetailModel: StudentDetailResponse) {
        _studentData.value = studentDetailModel
    }
}