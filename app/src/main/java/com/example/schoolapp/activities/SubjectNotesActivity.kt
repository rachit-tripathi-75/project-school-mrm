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
import com.example.schoolapp.databinding.ActivitySubjectNotesBinding
import com.example.schoolapp.fragments.ActivityDetailFragment

class SubjectNotesActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySubjectNotesBinding
    private lateinit var subjectNotesAdapter: SubjectNotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySubjectNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()
        setupSubjectNotesRecyclerView()
        loadSubjectNotesData()
        listeners()
    }

    private fun initialisers() {


    }

    private fun listeners() {
        binding.ivBack.setOnClickListener{
            onBackPressed();
        }
    }

    private fun setupSubjectNotesRecyclerView() {
        binding.rvSubjectNotes.layoutManager = LinearLayoutManager(this)

        subjectNotesAdapter = SubjectNotesAdapter()
        binding.rvSubjectNotes.adapter = subjectNotesAdapter
    }

    private fun loadSubjectNotesData() {
        // loaded data from a database or API........

        val records = mutableListOf<SubjectNotesRecord>()


        records.add(SubjectNotesRecord("Math", "Calculus", "Calculus.pdf"))
        records.add(SubjectNotesRecord("Math", "Calculus", "Calculus.pdf"))
        records.add(SubjectNotesRecord("Math", "Calculus", "Calculus.pdf"))
        records.add(SubjectNotesRecord("Math", "Calculus", "Calculus.pdf"))
        records.add(SubjectNotesRecord("Math", "Calculus", "Calculus.pdf"))
        records.add(SubjectNotesRecord("Math", "Calculus", "Calculus.pdf"))
        records.add(SubjectNotesRecord("Math", "Calculus", "Calculus.pdf"))
        records.add(SubjectNotesRecord("Math", "Calculus", "Calculus.pdf"))
        records.add(SubjectNotesRecord("Math", "Calculus", "Calculus.pdf"))


        subjectNotesAdapter.setSubjectNotesRecords(records)
    }

    data class SubjectNotesRecord(val subject: String, val detail: String, val file: String)

    private inner class SubjectNotesAdapter : RecyclerView.Adapter<SubjectNotesAdapter.ViewHolder>() {
        private var subjectNotesRecords = listOf<SubjectNotesRecord>()

        fun setSubjectNotesRecords(records: List<SubjectNotesRecord>) {
            this.subjectNotesRecords = records
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_subject_notes, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = subjectNotesRecords[position]
            holder.tvSubject.text = record.subject
            holder.tvDetail.text = record.detail
            holder.tvFile.text = record.file

        }

        override fun getItemCount() = subjectNotesRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
            val tvDetail: TextView = itemView.findViewById(R.id.tvDetail)
            val tvFile: TextView = itemView.findViewById(R.id.tvFile)
        }
    }

}