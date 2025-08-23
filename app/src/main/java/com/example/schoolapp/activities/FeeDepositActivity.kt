package com.example.schoolapp.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.schoolapp.R
import com.example.schoolapp.activities.PersonalInformationActivity
import com.example.schoolapp.adapters.FeeDepositViewPagerAdapter
import com.example.schoolapp.adapters.StudentCommentViewPagerAdapter
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.ActivityFeeDepositBinding
import com.example.schoolapp.fragments.PaymentHistoryFragment
import com.example.schoolapp.networks.NetworkChangeReceiver
import com.example.schoolapp.requests.PaymentHistoryRequest
import com.example.schoolapp.responses.FeeInstallmentsResponse
import com.example.schoolapp.responses.PaymentHistoryResponse
import com.example.schoolapp.responses.StudentDetailResponse
import com.example.schoolapp.viewmodels.FeeInstallmentViewModel
import com.example.schoolapp.viewmodels.PaymentHistoryViewModel
import com.example.schoolapp.viewmodels.StudentDetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import kotlin.getValue

class FeeDepositActivity : AppCompatActivity() {


    var networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
        override fun onNetworkConnected() {
            binding.llNoInternetFound.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE
            initialisers()
            fetchPaymentHistoryInformation()
            Log.d("networkInterceptorTAG", "inside onNetworkConnected()")

        }

        override fun onNetworkDisconnected() {
            binding.viewPager.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
            binding.llNoInternetFound.visibility = View.VISIBLE
            Log.d("networkInterceptorTAG", "inside onNetworkDisconnected()")
            Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_LONG).show()
        }
    })


    private lateinit var binding: ActivityFeeDepositBinding
    private val paymentHistoryViewModel: PaymentHistoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFeeDepositBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()

    }

    private fun initialisers() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.viewPager.adapter = FeeDepositViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Pay Fees"
                1 -> "Payment History"
                else -> ""
            }
        }.attach()
    }



    private fun fetchPaymentHistoryInformation() {
        binding.viewPager.visibility = View.GONE
        binding.llProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                delay(1500)
                val s = PaymentHistoryRequest(PrefsManager.getUserDetailedInformation(applicationContext).studentData.get(0).enrollment) // this is enrollment number, ye preference manager se fetch hoga......!!!
                ApiClient.paymentHistoryInstance.getStudentPaymentHistory(
                    "Bearer TOKEN_REQUIRED......",
                    "application/json",
                    "ci_session=idu31cp5tlrn9cbvfj203b7use5550ou",
                    s).enqueue(object: retrofit2.Callback<PaymentHistoryResponse> {

                    override fun onResponse(call: Call<PaymentHistoryResponse?>, response: Response<PaymentHistoryResponse?>) {
                        binding.viewPager.visibility = View.VISIBLE
                        binding.llProgressBar.visibility = View.GONE
                        binding.tabLayout.visibility = View.VISIBLE
                        if (response.isSuccessful && response.body() != null) {
                            val s = response.body()
                            val gson = Gson()
                            if (s?.status == 1) {
                                Log.d("paymentHistoryDetailTAG", "${gson.toJson(s)}}")
                                paymentHistoryViewModel.setPaymentHistoryData(s)
                            }
                        }
                    }

                    override fun onFailure(call: Call<PaymentHistoryResponse?>, t: Throwable) {
                        binding.viewPager.visibility = View.VISIBLE
                        binding.llProgressBar.visibility = View.GONE
                        Log.d("paymentHistoryDetailTAG", "${t.message}")
                    }

                })
            } catch (e: Exception) {
                Toast.makeText(this@FeeDepositActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        NetworkChangeReceiver.registerReceiver(this, networkChangeReceiver)
    }

    override fun onPause() {
        super.onPause()
        NetworkChangeReceiver.unregisterReceiver(this, networkChangeReceiver)
    }


}