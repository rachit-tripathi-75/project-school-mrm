package com.example.schoolapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.schoolapp.responses.PaymentHistoryResponse

class PaymentHistoryViewModel : ViewModel() {
    private val _paymentHistoryData = MutableLiveData<PaymentHistoryResponse>()
    val paymentHistoryData: LiveData<PaymentHistoryResponse> get() = _paymentHistoryData
    fun setPaymentHistoryData(paymentHistoryDetailModel: PaymentHistoryResponse) {
        _paymentHistoryData.value = paymentHistoryDetailModel
    }
}