package com.example.schoolapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.databinding.FragmentGrievanceListBinding
import com.google.android.material.chip.Chip


class GrievanceListFragment : Fragment() {
    private lateinit var binding: FragmentGrievanceListBinding
    private lateinit var grievanceListAdapter: GrievanceListAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentGrievanceListBinding.inflate(inflater, container, false)

        initialisers()

        return binding.root
    }

    private fun initialisers() {
        setGrievanceRecyclerView()
        loadGrievanceData()
    }

    private fun setGrievanceRecyclerView() {

        binding.grievancesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        grievanceListAdapter = GrievanceListAdapter()
        binding.grievancesRecyclerView.adapter = grievanceListAdapter
    }

    private fun loadGrievanceData() {
        // loaded data from a database or API........

        val records = mutableListOf<GrievanceRecord>()


        // Add some dummy data for other PTA Meetings......
        records.add(GrievanceRecord("Inadequate Lighting in Classroom B-12", "In Progress", "April 25, 2025", "The lighting in classroom B-12 is insufficient, making it difficult to read and take notes during afternoon classes.", "School Auditorium", "1 response"))
        records.add(GrievanceRecord("Inadequate Lighting in Classroom B-12", "In Progress", "April 25, 2025", "The lighting in classroom B-12 is insufficient, making it difficult to read and take notes during afternoon classes.", "School Auditorium", "1 response"))
        records.add(GrievanceRecord("Inadequate Lighting in Classroom B-12", "In Progress", "April 25, 2025", "The lighting in classroom B-12 is insufficient, making it difficult to read and take notes during afternoon classes.", "School Auditorium", "1 response"))
        records.add(GrievanceRecord("Inadequate Lighting in Classroom B-12", "In Progress", "April 25, 2025", "The lighting in classroom B-12 is insufficient, making it difficult to read and take notes during afternoon classes.", "School Auditorium", "1 response"))
        records.add(GrievanceRecord("Inadequate Lighting in Classroom B-12", "In Progress", "April 25, 2025", "The lighting in classroom B-12 is insufficient, making it difficult to read and take notes during afternoon classes.", "School Auditorium", "1 response"))
        records.add(GrievanceRecord("Inadequate Lighting in Classroom B-12", "In Progress", "April 25, 2025", "The lighting in classroom B-12 is insufficient, making it difficult to read and take notes during afternoon classes.", "School Auditorium", "1 response"))
        records.add(GrievanceRecord("Inadequate Lighting in Classroom B-12", "In Progress", "April 25, 2025", "The lighting in classroom B-12 is insufficient, making it difficult to read and take notes during afternoon classes.", "School Auditorium", "1 response"))
        records.add(GrievanceRecord("Inadequate Lighting in Classroom B-12", "In Progress", "April 25, 2025", "The lighting in classroom B-12 is insufficient, making it difficult to read and take notes during afternoon classes.", "School Auditorium", "1 response"))

        grievanceListAdapter.setPtaMeetings(records)
    }

    data class GrievanceRecord(val grievanceTitle: String, val status: String, val grievanceDate: String, val grievanceDescription: String, val category: String, val responseCount: String)

    private inner class GrievanceListAdapter :
        RecyclerView.Adapter<GrievanceListFragment.GrievanceListAdapter.ViewHolder>() {
        private var grievanceRecords = listOf<GrievanceRecord>()

        fun setPtaMeetings(records: List<GrievanceRecord>) {
            this.grievanceRecords = records
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_grievance, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = grievanceRecords[position]
            holder.tvGrievanceTitle.text = record.grievanceTitle
            holder.chipStatus.text = record.status
            holder.tvGrievanceDate.text = record.grievanceDate
            holder.tvGrievanceDescription.text = record.grievanceDescription
            holder.chipCategory.text = record.category
            holder.tvResponseCount.text = record.responseCount

        }

        override fun getItemCount() = grievanceRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvGrievanceTitle: TextView = itemView.findViewById(R.id.tvGrievanceTitle)
            val chipStatus: Chip = itemView.findViewById(R.id.chipStatus)
            val tvGrievanceDate: TextView = itemView.findViewById(R.id.tvGrievanceDate)
            val tvGrievanceDescription: TextView = itemView.findViewById(R.id.tvGrievanceDescription)
            val chipCategory: Chip = itemView.findViewById(R.id.chipCategory)
            val tvResponseCount: TextView = itemView.findViewById(R.id.tvResponseCount)
        }
    }



}