package com.example.schoolapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolapp.responses.FeeInstallmentDetailResponse

class FeeInstallmentDetailsViewModel : ViewModel() {

    private val _feeData = MutableLiveData<FeeInstallmentDetailResponse>()
    val feeData: LiveData<FeeInstallmentDetailResponse> get() = _feeData
    fun setFeeData(feeInstallmentDetailModel: FeeInstallmentDetailResponse) {
        _feeData.value = feeInstallmentDetailModel
    }

}