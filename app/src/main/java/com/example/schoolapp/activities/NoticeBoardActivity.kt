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
import com.example.schoolapp.databinding.ActivityNoticeBoardBinding
import com.google.android.material.chip.Chip

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
    }

    private fun initialisers() {
        setupNoticeBoardRecyclerView()
        loadNoticeBoardData()
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }

    private fun setupNoticeBoardRecyclerView() {

        binding.noticeRecyclerView.layoutManager = LinearLayoutManager(this)

        noticeBoardAdapter = NoticeBoardAdapter()
        binding.noticeRecyclerView.adapter = noticeBoardAdapter
    }

    private fun loadNoticeBoardData() {
        // loaded data from a database or API........

        val records = mutableListOf<NoticeBoardRecord>()


        // Add some dummy data for other months
        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))
        records.add(NoticeBoardRecord("Annual Sports Day", "24 April 2025", "The annual sports day will be held on May 15th. All students are required to participate in at least one event. Registration closes on May 5th.", "Event"))

        noticeBoardAdapter.setNoticeBoard(records)
    }

    data class NoticeBoardRecord(val noticeTitle: String, val noticeDate: String, val noticeDescription: String, val category: String)

    private inner class NoticeBoardAdapter :
        RecyclerView.Adapter<NoticeBoardActivity.NoticeBoardAdapter.ViewHolder>() {
        private var attendanceRecords = listOf<NoticeBoardRecord>()

        fun setNoticeBoard(records: List<NoticeBoardRecord>) {
            this.attendanceRecords = records
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_notice, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = attendanceRecords[position]
            holder.tvNoticeTitle.text = record.noticeTitle
            holder.tvNoticeDate.text = record.noticeDate
            holder.tvNoticeDescription.text = record.noticeDescription
            holder.chipCategory.text = record.category

        }

        override fun getItemCount() = attendanceRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvNoticeTitle: TextView = itemView.findViewById(R.id.tvNoticeTitle)
            val tvNoticeDate: TextView = itemView.findViewById(R.id.tvNoticeDate)
            val tvNoticeDescription: TextView = itemView.findViewById(R.id.tvNoticeDescription)
            val chipCategory: Chip = itemView.findViewById(R.id.chipCategory)
        }
    }
}