package com.example.schoolapp.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.ApiServices
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.ActivityHomeWorkBinding
import com.example.schoolapp.networks.NetworkChangeReceiver
import com.example.schoolapp.requests.PaymentHistoryRequest
import com.example.schoolapp.responses.HomeworkResponse
import com.example.schoolapp.responses.PaymentHistoryResponse
import com.example.schoolapp.viewmodels.FeeInstallmentViewModel
import com.example.schoolapp.viewmodels.HomeworkViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import kotlin.getValue

class HomeWorkActivity : AppCompatActivity() {


    var networkChangeReceiver: NetworkChangeReceiver =
        NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
            override fun onNetworkConnected() {
                binding.llNoInternetFound.visibility = View.GONE
                binding.llNoDataFound.visibility = View.GONE
                binding.clAllContent.visibility = View.VISIBLE
                initialisers()
                Log.d("networkInterceptorTAG", "inside onNetworkConnected()")

            }

            override fun onNetworkDisconnected() {
                binding.clAllContent.visibility = View.GONE
                binding.llNoDataFound.visibility = View.GONE
                binding.llNoInternetFound.visibility = View.VISIBLE
                Log.d("networkInterceptorTAG", "inside onNetworkDisconnected()")
                Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_LONG).show()
            }
        })


    private lateinit var binding: ActivityHomeWorkBinding
    private lateinit var homeworkAdapter: HomeworkAdapter
    private val homeworkViewModel: HomeworkViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHomeWorkBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        initialisers()
        listeners()
    }

    private fun initialisers() {
        // Set up attendance recycler view
        setupSubjectNotesRecyclerView()
        loadSubjectNotesData()
    }

    private fun listeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed();
        }
    }

    private fun setupSubjectNotesRecyclerView() {
        binding.homeworkRecyclerView.layoutManager = LinearLayoutManager(this)

        homeworkAdapter = HomeworkAdapter()
        binding.homeworkRecyclerView.adapter = homeworkAdapter
    }

    private fun loadSubjectNotesData() {
        // loaded data from a database or API........
        binding.progressBar.visibility = View.VISIBLE
        binding.homeworkRecyclerView.visibility = View.GONE
        binding.llNoDataFound.visibility = View.GONE
        binding.clAllContent.visibility = View.GONE
        binding.llInternalServerError.visibility = View.GONE

        lifecycleScope.launch {
            try {
                delay(1500)
                ApiClient.homeworkInstance.getHomework(
                    "Bearer TOKEN_REQUIRED......",
                    "application/x-www-form-urlencoded",
                    "ci_session=idu31cp5tlrn9cbvfj203b7use5550ou",
                    PrefsManager.getSectionId(applicationContext)).enqueue(object: retrofit2.Callback<HomeworkResponse> {

                    override fun onResponse(call: Call<HomeworkResponse?>, response: Response<HomeworkResponse?>) {
                        binding.homeworkRecyclerView.visibility = View.VISIBLE
                        binding.progressBar.visibility = View.GONE

                        if (response.isSuccessful && response.body() != null) {
                            val s = response.body()
                            val gson = Gson()
                            if (s?.status == 1 && s.data.isNotEmpty()) {
                                binding.clAllContent.visibility = View.VISIBLE
                                Log.d("homeworkTAG", "${gson.toJson(s)}}")
                                homeworkViewModel.setHomeworkData(s)
                                setDataToRecyclerView()
                            } else {
                                binding.homeworkRecyclerView.visibility = View.GONE
                                binding.llNoDataFound.visibility = View.VISIBLE
                            }
                        }
                    }

                    override fun onFailure(call: Call<HomeworkResponse?>, t: Throwable) {
                        binding.clAllContent.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.llInternalServerError.visibility = View.VISIBLE
                        Log.d("homeworkTAG", "${t.message}")
                    }

                })
            } catch (e: Exception) {
                Log.d("homeworkTAG", "${e.message}")
                Toast.makeText(this@HomeWorkActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setDataToRecyclerView() {
        val records = mutableListOf<HomeworkRecord>()

        homeworkViewModel.homeworkData.observe(this) { homeworkData ->
            val gson = Gson()
            Log.d("fetchxxxx", gson.toJson(homeworkData))
            for (i in 0 until homeworkData.data.size) {
                records.add(HomeworkRecord(homeworkData.data[i].id, homeworkData.data[i].subject, homeworkData.data[i].date.toString()))

            }
        }






        homeworkAdapter.setHomeworkRecords(records)
    }

    data class HomeworkRecord(
        val id: String,
        val subject: String,
        val date: String
    )

    private inner class HomeworkAdapter : RecyclerView.Adapter<HomeworkAdapter.ViewHolder>() {
        private var homeworkRecords = listOf<HomeworkRecord>()

        fun setHomeworkRecords(records: List<HomeworkRecord>) {
            this.homeworkRecords = records
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_homework, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = homeworkRecords[position]
            holder.tvSubject.text = record.subject
            holder.tvTopic.text = record.id
            holder.tvRemark.text = record.date

        }

        override fun getItemCount() = homeworkRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
            val tvTopic: TextView = itemView.findViewById(R.id.tvTopic)
            val tvRemark: TextView = itemView.findViewById(R.id.tvRemark)
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