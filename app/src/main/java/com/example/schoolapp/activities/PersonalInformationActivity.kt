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
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.schoolapp.R
import com.example.schoolapp.adapters.FeeDepositViewPagerAdapter
import com.example.schoolapp.adapters.PersonalInformationViewPagerAdapter
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.ApiServices
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.ActivityPersonalInformationBinding
import com.example.schoolapp.models.StudentDetailModel
import com.example.schoolapp.networks.NetworkChangeReceiver
import com.example.schoolapp.responses.GetMarksResponse
import com.example.schoolapp.responses.StudentDetailResponse
import com.example.schoolapp.viewmodels.StudentDetailViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class PersonalInformationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPersonalInformationBinding
    private val studentDetailViewModel: StudentDetailViewModel by viewModels()


    var networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
        override fun onNetworkConnected() {
            binding.llNoInternetFound.visibility = View.GONE
            binding.viewPager.visibility = View.VISIBLE
            initialisers()
            fetchStudentData()
            Log.d("networkInterceptorTAG", "inside onNetworkConnected()")

        }

        override fun onNetworkDisconnected() {
            binding.viewPager.visibility = View.GONE
            binding.llNoInternetFound.visibility = View.VISIBLE
            Log.d("networkInterceptorTAG", "inside onNetworkDisconnected()")
            Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_LONG).show()
        }
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPersonalInformationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()
        fetchStudentData()
    }

    private fun initialisers() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.viewPager.adapter = PersonalInformationViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Basic Info"
                1 -> "personal Info"
                else -> ""
            }
        }.attach()
    }

    private fun fetchStudentData() {
        binding.viewPager.visibility = View.GONE
        binding.llProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch {
            try {
                delay(1500)
                ApiClient.getStudentDetailInstance.getStudentDetail(
                    "Bearer 8a56598bd5114ab31f6f70e76e1873e8945eafcd915b3f6ada4c0132d212a57e",
                    "ci_session=9muik1cd5884084kjfnq9vo3d3k5fhfu",
                    "1675").enqueue(object: retrofit2.Callback<StudentDetailResponse> {


                    override fun onResponse(call: Call<StudentDetailResponse>, response: Response<StudentDetailResponse>) {
                        binding.viewPager.visibility = View.VISIBLE
                        binding.llProgressBar.visibility = View.GONE
                        if (response.isSuccessful && response.body() != null) {
                            val s = response.body()
                            val gson = Gson()
                            if (s?.status == 1) {
                                Log.d("studentDetailTAG", "${gson.toJson(s)}}")
                                studentDetailViewModel.setStudentData(s)
                            }
                        }
                    }

                    override fun onFailure(call: Call<StudentDetailResponse>, t: Throwable) {
                        binding.viewPager.visibility = View.VISIBLE
                        binding.llProgressBar.visibility = View.GONE
                        Log.d("studentDetailTAG", "${t.message}")
                    }

                })
            } catch (e: Exception) {
                Toast.makeText(this@PersonalInformationActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
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