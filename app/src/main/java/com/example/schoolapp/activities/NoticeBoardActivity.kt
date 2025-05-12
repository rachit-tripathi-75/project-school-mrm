package com.example.schoolapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.MainActivity
import com.example.schoolapp.R
import com.example.schoolapp.activities.LoginActivity
import com.example.schoolapp.adapters.NoticeBoardAdapter
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.ActivityNoticeBoardBinding
import com.example.schoolapp.responses.LoginResponse
import com.example.schoolapp.responses.NoticeDetailResponse
import com.google.android.material.chip.Chip
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class NoticeBoardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoticeBoardBinding
    private lateinit var noticeBoardAdapter: NoticeBoardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNoticeBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initialisers()
        listeners()
    }

    private fun initialisers() {
        setupNoticeBoardRecyclerView()
        loadNoticeBoardData()
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun listeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            loadNoticeBoardData()
        }

        binding.chipAll.setOnClickListener {
            noticeBoardAdapter.filter(binding.chipAll.text.toString())
        }
        binding.chipAcademic.setOnClickListener {
            noticeBoardAdapter.filter(binding.chipAcademic.text.toString())
        }

        binding.chipEvents.setOnClickListener {
            noticeBoardAdapter.filter(binding.chipEvents.text.toString())
        }

        binding.chipAnnouncements.setOnClickListener {
            noticeBoardAdapter.filter(binding.chipAnnouncements.text.toString())
        }


    }

    private fun setupNoticeBoardRecyclerView() {

        binding.rvNotice.layoutManager = LinearLayoutManager(this)

        noticeBoardAdapter = NoticeBoardAdapter(this)
        binding.rvNotice.adapter = noticeBoardAdapter
    }

    private fun loadNoticeBoardData() {
        // loaded data from a database or API........

        binding.rvNotice.visibility = View.GONE
        binding.containerLoading.visibility = View.VISIBLE

        ApiClient.getNoticeDetailInstance.getNoticeDetail(
            "Bearer 8a56598bd5114ab31f6f70e76e1873e8945eafcd915b3f6ada4c0132d212a57e",
            "ci_session=21fd32pu9g5q2i3501kadta47r3uip7k").enqueue(object : retrofit2.Callback<NoticeDetailResponse> {


            override fun onResponse(call: Call<NoticeDetailResponse?>, response: Response<NoticeDetailResponse?>) {
                if (response.isSuccessful) {
                    if (binding.swipeRefreshLayout.isRefreshing) {
                        binding.swipeRefreshLayout.isRefreshing = false
                    }
                    binding.rvNotice.visibility = View.VISIBLE
                    binding.containerLoading.visibility = View.GONE
                    val responseBody = response.body()
                    if (responseBody?.status == 1) {
                        val notices = responseBody

                        if (notices.data.isEmpty()) {
                            Toast.makeText(this@NoticeBoardActivity, "No notices found", Toast.LENGTH_SHORT).show()
                            return
                        }

                        val records = notices.data.map { s ->
                            NoticeBoardRecord(notices.path, s.id, s.title, s.date_from, s.date, s.type, s.doc, s.created_on, s.status, s.NoticeType, s.sessionid, s.created_on)
                        }

                        noticeBoardAdapter.setNoticeBoard(records)
                    } else {
                        // Handle case where status != 1
                        Log.d("noticeResponseTAG", "API returned status: ${responseBody?.status}")
                    }
                } else {
                    // Handle unsuccessful response
                    binding.rvNotice.visibility = View.VISIBLE
                    binding.containerLoading.visibility = View.GONE
                    Log.d("noticeResponseTAG", "Response not successful: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<NoticeDetailResponse?>, t: Throwable) {
//                binding.btnSignIn.visibility = View.VISIBLE
//                binding.progressBar.visibility = View.GONE
                Log.d("noticeResponseTAG", "Error: ${t.message}")
                binding.rvNotice.visibility = View.VISIBLE
                binding.containerLoading.visibility = View.GONE
                Toast.makeText(this@NoticeBoardActivity, "An error has occurred. Please try again later", Toast.LENGTH_SHORT).show()
            }
        })

        // Add some dummy data for other months
//        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
//        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
//        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
//        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
//        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
//        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
//        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
//
//        noticeBoardAdapter.setNoticeBoard(records)
    }



    data class NoticeBoardRecord(val path: String, val id: String, val noticeTitle: String, val dateFrom: String, val noticeDate: String, val type: String, val pdfName: String, val createdOn: String, val status: String, val notifyType: String, val sessionId: String, val noticeDescription: String)
}