package com.example.schoolapp.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.databinding.ActivityAcademicCalendarBinding
import com.google.android.material.chip.Chip

class AcademicCalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAcademicCalendarBinding
    private lateinit var upcomingEventsAdapter: UpcomingEventsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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

    }

    private fun initialisers() {
        setupAcademicCalendarRecyclerView()
        loadAcademicCalendarData()
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupAcademicCalendarRecyclerView() {
        binding.eventsRecyclerView.layoutManager = LinearLayoutManager(this)

        upcomingEventsAdapter = UpcomingEventsAdapter()
        binding.eventsRecyclerView.adapter = upcomingEventsAdapter
    }

    private fun loadAcademicCalendarData() {
        // loaded data from a database or API........

        val records = mutableListOf<UpcomingEventRecord>()


        // Add some dummy data for other upcoming events.......
        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))
        records.add(UpcomingEventRecord("Final Exams Begin", "Exam", "Monday, May 15 2025", "Final examinations for all classes begin today."))


        upcomingEventsAdapter.setUpcomingEvents(records)
    }

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

}