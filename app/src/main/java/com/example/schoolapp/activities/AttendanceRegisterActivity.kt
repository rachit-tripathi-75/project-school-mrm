package com.example.schoolapp.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.activities.FeeDepositActivity
import com.example.schoolapp.adapters.AttendanceAdapter
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.ActivityAttendanceRegisterBinding
import com.example.schoolapp.fragments.PaymentHistoryFragment.PaymentHistoryRecord
import com.example.schoolapp.models.AttendanceRecordModel
import com.example.schoolapp.networks.NetworkChangeReceiver
import com.example.schoolapp.requests.PaymentHistoryRequest
import com.example.schoolapp.responses.AttendanceResponse
import com.example.schoolapp.responses.PaymentHistoryResponse
import com.example.schoolapp.viewmodels.AttendanceViewModel
import com.example.schoolapp.viewmodels.FeeInstallmentViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.util.Calendar
import java.util.function.LongFunction
import kotlin.getValue

class AttendanceRegisterActivity : AppCompatActivity() {



    var networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
        override fun onNetworkConnected() {
            binding.llNoInternetFound.visibility = View.GONE
            binding.clAllContent.visibility = View.VISIBLE
            initialisers()
            listeners()
            setupMonthSpinner()
            setupAttendanceRecyclerView()
            Log.d("networkInterceptorTAG", "inside onNetworkConnected()")

        }

        override fun onNetworkDisconnected() {
            binding.clAllContent.visibility = View.GONE
            binding.llNoInternetFound.visibility = View.VISIBLE
            Log.d("networkInterceptorTAG", "inside onNetworkDisconnected()")
            Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_LONG).show()
        }
    })




    private lateinit var binding: ActivityAttendanceRegisterBinding
    private lateinit var attendanceAdapter: AttendanceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAttendanceRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initialisers()
        listeners()
        setupMonthSpinner()
        setupAttendanceRecyclerView()
        
    }

    private fun initialisers() {


    }

    private fun listeners() {
        binding.btnShow.setOnClickListener {
            val selectedMonth = binding.spinnerMonth.selectedItem.toString()
            when (selectedMonth) {
                "Jan" -> loadAttendanceData("1")
                "Feb" -> loadAttendanceData("2")
                "Mar" -> loadAttendanceData("3")
                "Apr" -> loadAttendanceData("4")
                "May" -> loadAttendanceData("5")
                "Jun" -> loadAttendanceData("6")
                "Jul" -> loadAttendanceData("7")
                "Aug" -> loadAttendanceData("8")
                "Sep" -> loadAttendanceData("9")
                "Oct" -> loadAttendanceData("10")
                "Nov" -> loadAttendanceData("11")
                "Dec" -> loadAttendanceData("12")
                else -> loadAttendanceData("0")
            }

        }

        binding.ivBack.setOnClickListener {
            onBackPressed();
        }
    }


    private fun setupMonthSpinner() {
        // Create array of months
        val months = arrayOf(
            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
        )


        val monthAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            months
        )


        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)


        binding.spinnerMonth.adapter = monthAdapter


        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        binding.spinnerMonth.setSelection(currentMonth)
    }

    private fun setupAttendanceRecyclerView() {
        binding.attendanceRecyclerView.layoutManager = LinearLayoutManager(this)

        attendanceAdapter = AttendanceAdapter()
        binding.attendanceRecyclerView.adapter = attendanceAdapter
    }

    private fun loadAttendanceData(month: String) {
        // loaded data from a database or API........

        Log.d("herexx", "here0")
        binding.progressBar.visibility = View.VISIBLE
        binding.attendanceRecyclerView.visibility = View.GONE
        binding.llNoDataFound.visibility = View.GONE
        binding.llInternalServerError.visibility = View.GONE
        Log.d("monthxxx", month)
        Log.d("studentIdxxx", PrefsManager.getUserInformation(applicationContext).data.stu_id);

        lifecycleScope.launch {
            try {
                delay(1500)
                ApiClient.attendanceInstance.getStudentAttendance(
                    "Bearer YOUR_TOKEN",
                    "application/x-www-form-urlencoded",
                    "ci_session=2t8pu97bd55ljkpucvq2jlt74fklsrhg",
                    PrefsManager.getUserDetailedInformation(applicationContext).studentData.get(0).sidInc,
                    month).enqueue(object: retrofit2.Callback<AttendanceResponse> {

                    override fun onResponse(call: Call<AttendanceResponse?>, response: Response<AttendanceResponse?>) {
                        binding.progressBar.visibility = View.GONE
                        binding.attendanceRecyclerView.visibility = View.VISIBLE
                        if (response.isSuccessful && response.body() != null) {
                            val s = response.body()
                            val gson = Gson()
                            if (s?.status == 1 && s.data.isNotEmpty()) {
                                Log.d("herexx", "here1")
                                Log.d("attendanceTAG", "${PrefsManager.getUserInformation(applicationContext).data.stu_id}")
                                setDataToAdapter(s)

                            } else {
                                binding.llNoDataFound.visibility = View.VISIBLE
                                binding.attendanceRecyclerView.visibility = View.GONE
                                Log.d("herexx", "here2")
                            }
                        } else {
                            // will only be here, if either student_id or month is entered wrong, which never gonna occur
                        }
                    }

                    override fun onFailure(call: Call<AttendanceResponse?>, t: Throwable) {
                        binding.progressBar.visibility = View.GONE
                        binding.attendanceRecyclerView.visibility = View.GONE
                        binding.llInternalServerError.visibility = View.VISIBLE
                        Log.d("attendanceTAG", "${t.message}")
                        Log.d("herexx", "here3")
                    }

                })
            } catch (e: Exception) {
                Toast.makeText(this@AttendanceRegisterActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("herexx", "here4")
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

    private fun setDataToAdapter(s: AttendanceResponse) {
        val records = mutableListOf<AttendanceRecordModel>()


            for (i in 0 until s.data.size) {
                records.add(AttendanceRecordModel(s.data[i].date, s.data[i].attendance))
            }

        attendanceAdapter.setAttendanceRecords(records)


        }

    }


