package com.example.schoolapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolapp.responses.FeeInstallmentsResponse

class FeeInstallmentViewModel : ViewModel() {
    private val _feeInstallmentData = MutableLiveData<FeeInstallmentsResponse>()
    val feeInstallmentData: LiveData<FeeInstallmentsResponse> get() = _feeInstallmentData
    fun setFeeInstallmentData(feeInstallmentModel: FeeInstallmentsResponse) {
        _feeInstallmentData.value = feeInstallmentModel
    }
}