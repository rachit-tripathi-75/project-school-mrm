package com.example.schoolapp.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.activities.HomeWorkActivity
import com.example.schoolapp.adapters.AcademicPlanAdapter
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.ActivityAcademicCalendarBinding
import com.example.schoolapp.networks.NetworkChangeReceiver
import com.example.schoolapp.responses.AcademicPlan
import com.example.schoolapp.responses.AcademicPlanResponse
import com.example.schoolapp.responses.HomeworkResponse
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response
import java.security.Provider
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class AcademicCalendarActivity : AppCompatActivity() {



    var networkChangeReceiver: NetworkChangeReceiver = NetworkChangeReceiver(object : NetworkChangeReceiver.NetworkStatusListener {
            override fun onNetworkConnected() {
                binding.llNoInternetFound.visibility = View.GONE
                binding.clAllContent.visibility = View.VISIBLE
                initialisers()
                setupRecyclerView()
                loadAcademicPlans()
                Log.d("networkInterceptorTAG", "inside onNetworkConnected()")

            }

            override fun onNetworkDisconnected() {
                binding.clAllContent.visibility = View.GONE
                binding.llNoInternetFound.visibility = View.VISIBLE
                Log.d("networkInterceptorTAG", "inside onNetworkDisconnected()")
                Snackbar.make(binding.root, "No Internet Connection", Snackbar.LENGTH_LONG).show()
            }
        })


    private lateinit var binding: ActivityAcademicCalendarBinding

    private lateinit var adapter: AcademicPlanAdapter



    private lateinit var upcomingEventsAdapter: UpcomingEventsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAcademicCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()
        setupRecyclerView()
        loadAcademicPlans()


    }



    private fun setupRecyclerView() {
        adapter = AcademicPlanAdapter(emptyList()) { academicPlan ->
            onAcademicPlanClicked(academicPlan)
        }

        binding.recyclerViewAcademicPlans.apply {
            layoutManager = LinearLayoutManager(this@AcademicCalendarActivity)
            adapter = this@AcademicCalendarActivity.adapter
            setHasFixedSize(true)
        }
    }

    private fun setupRetrofit() {

    }

    private fun loadAcademicPlans() {
        showLoading(true)



        lifecycleScope.launch {
            try {
                delay(1500)
                ApiClient.academicPlanInstance.getAcademicPlan(
                    "Bearer TOKEN_REQUIRED......",
                    "application/x-www-form-urlencoded",
                    "ci_session=57gfrfq76muv5vpaod4ffdfg64ttu14f").enqueue(object: retrofit2.Callback<AcademicPlanResponse> {

                    override fun onResponse(call: Call<AcademicPlanResponse?>, response: Response<AcademicPlanResponse?>) {
                        showLoading(false)

                        if (response.isSuccessful) {
                            val academicPlanResponse = response.body()
                            if (academicPlanResponse != null && academicPlanResponse.status == 1 && academicPlanResponse.type == "success") {

                                Log.d("academicPlanTAG", Gson().toJson(response.body()))
                                displayAcademicPlans(academicPlanResponse.data)
                            } else {
                                Log.d("academicPlanTAG", Gson().toJson(response.body()))
                                showError(academicPlanResponse?.message ?: "Failed to load academic plans")
                            }
                        } else {
                            Log.d("academicPlanTAG", Gson().toJson(response.body()))
                            showError("Network error: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<AcademicPlanResponse?>, t: Throwable) {
                        Log.d("academicPlanTAG", Gson().toJson(t.message))
                        showLoading(false)
                        showError("Network error: ${t.message}")
                    }

                })
            } catch (e: Exception) {
                Log.d("homeworkTAG", "${e.message}")
                Toast.makeText(this@AcademicCalendarActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun displayAcademicPlans(plans: List<AcademicPlan>) {
        if (plans.isEmpty()) {
            showEmptyState(true)
        } else {
            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

            val filteredList = plans.filter { plan ->
                plan.announcedLater == "0" || try {
                    val fromDate = LocalDate.parse(plan.fromDate, formatter)
                    val toDate = LocalDate.parse(plan.toDate, formatter)
                    !currentDate.isBefore(fromDate) && !currentDate.isAfter(toDate)
                } catch (e: Exception) {
                    false
                }
            }

            showEmptyState(filteredList.isEmpty())
            adapter.updateData(filteredList)
        }
    }

    private fun showLoading(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerViewAcademicPlans.visibility = if (show) View.GONE else View.VISIBLE
        binding.emptyStateLayout.visibility = View.GONE
    }

    private fun showEmptyState(show: Boolean) {
        binding.emptyStateLayout.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerViewAcademicPlans.visibility = if (show) View.GONE else View.VISIBLE
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
        showEmptyState(true)
    }

    private fun onAcademicPlanClicked(academicPlan: AcademicPlan) {
        // Handle academic plan click
        Toast.makeText(this, "Clicked: ${academicPlan.title}", Toast.LENGTH_SHORT).show()

        // You can navigate to details activity here
        // val intent = Intent(this, AcademicPlanDetailsActivity::class.java)
        // intent.putExtra("academic_plan_id", academicPlan.id)
        // startActivity(intent)
    }
























    private fun initialisers() {
//        setupAcademicCalendarRecyclerView()
//        loadAcademicCalendarData()
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

//    private fun setupAcademicCalendarRecyclerView() {
//        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(this)
//
//        upcomingEventsAdapter = UpcomingEventsAdapter()
//        binding.eventsRecyclerView.adapter = upcomingEventsAdapter
//    }

//    private fun loadAcademicCalendarData() {
//        // loaded data from a database or API........
//
//        val records = mutableListOf<UpcomingEventRecord>()
//
//
//        // Add some dummy data for other upcoming events.......
//        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
//        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
//        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
//        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
//        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
//        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
//        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
//        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
//        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
//
//
//        upcomingEventsAdapter.setUpcomingEvents(records)
//    }

    data class UpcomingEventRecord(val eventTitle: String, val eventType: String, val eventDate: String, val eventDescription: String)

    private inner class UpcomingEventsAdapter :
        RecyclerView.Adapter<AcademicCalendarActivity.UpcomingEventsAdapter.ViewHolder>() {
        private var eventRecords = listOf<UpcomingEventRecord>()

        fun setUpcomingEvents(records: List<UpcomingEventRecord>) {
            this.eventRecords = records
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_calendar_event, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = eventRecords[position]
            holder.tvEventTitle.text = record.eventTitle
            holder.chipEventType.text = record.eventType
            holder.tvEventDate.text = record.eventDate
            holder.tvEventDescription.text = record.eventDescription

        }

        override fun getItemCount() = eventRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvEventTitle: TextView = itemView.findViewById(R.id.tvEventTitle)
            val chipEventType: Chip = itemView.findViewById(R.id.chipEventType)
            val tvEventDate: TextView = itemView.findViewById(R.id.tvEventDate)
            val tvEventDescription: TextView = itemView.findViewById(R.id.tvEventDescription)

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