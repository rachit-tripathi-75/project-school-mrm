package com.example.schoolapp.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R

class HomeWorkActivity : AppCompatActivity() {
    private lateinit var homeworkRecyclerView: RecyclerView
    private lateinit var ivBack: ImageView
    private lateinit var homeworkAdapter: HomeworkAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home_work)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initialisers()
        listeners()
    }

    private fun initialisers() {
        homeworkRecyclerView = findViewById(R.id.homeworkRecyclerView)
        ivBack = findViewById(R.id.ivBack)

        // Set up attendance recycler view
        setupSubjectNotesRecyclerView()
        loadSubjectNotesData()
    }

    private fun listeners() {
        ivBack.setOnClickListener{
            onBackPressed();
        }
    }

    private fun setupSubjectNotesRecyclerView() {
        homeworkRecyclerView.layoutManager = LinearLayoutManager(this)

        homeworkAdapter = HomeworkAdapter()
        homeworkRecyclerView.adapter = homeworkAdapter
    }

    private fun loadSubjectNotesData() {
        // loaded data from a database or API........

        val records = mutableListOf<HomeworkRecord>()


        records.add(HomeworkRecord("Math", "Trigonometry", "Trigonometry", "Trigonometry.pdf"))
        records.add(HomeworkRecord("Math", "Trigonometry", "Trigonometry", "Trigonometry.pdf"))
        records.add(HomeworkRecord("Math", "Trigonometry", "Trigonometry", "Trigonometry.pdf"))
        records.add(HomeworkRecord("Math", "Trigonometry", "Trigonometry", "Trigonometry.pdf"))
        records.add(HomeworkRecord("Math", "Trigonometry", "Trigonometry", "Trigonometry.pdf"))
        records.add(HomeworkRecord("Math", "Trigonometry", "Trigonometry", "Trigonometry.pdf"))
        records.add(HomeworkRecord("Math", "Trigonometry", "Trigonometry", "Trigonometry.pdf"))
        records.add(HomeworkRecord("Math", "Trigonometry", "Trigonometry", "Trigonometry.pdf"))
        records.add(HomeworkRecord("Math", "Trigonometry", "Trigonometry", "Trigonometry.pdf"))


        homeworkAdapter.setHomeworkRecords(records)
    }

    data class HomeworkRecord(val subject: String, val topic: String, val remark: String, val file: String)

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
            holder.tvTopic.text = record.topic
            holder.tvRemark.text = record.remark
            holder.tvFile.text = record.file

        }

        override fun getItemCount() = homeworkRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
            val tvTopic: TextView = itemView.findViewById(R.id.tvTopic)
            val tvRemark: TextView = itemView.findViewById(R.id.tvRemark)
            val tvFile: TextView = itemView.findViewById(R.id.tvFile)
        }
    }

}