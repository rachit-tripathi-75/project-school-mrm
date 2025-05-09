package com.example.schoolapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.databinding.FragmentPtaMeetingsBinding
import com.google.android.material.chip.Chip


class PtaMeetingsFragment : Fragment() {
    private lateinit var binding: FragmentPtaMeetingsBinding
    private lateinit var ptaMeetingsAdapter: PtaMeetingsAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPtaMeetingsBinding.inflate(inflater, container, false)

        initialisers()


        return binding.root
    }

    private fun initialisers() {
        setPtaMeetingRecyclerView()
        loadPtaMeetingsData()
    }

    private fun setPtaMeetingRecyclerView() {

        binding.meetingsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        ptaMeetingsAdapter = PtaMeetingsAdapter()
        binding.meetingsRecyclerView.adapter = ptaMeetingsAdapter
    }

    private fun loadPtaMeetingsData() {
        // loaded data from a database or API........

        val records = mutableListOf<PtaMeetingsRecord>()


        // Add some dummy data for other PTA Meetings......
        records.add(PtaMeetingsRecord("End of Term PTA Meeting", "Active", "May 20, 2025", "4:00 PM to 6:00PM", "School Auditorium", "Discussion on student performance, upcoming events, and feedback on school policies.", "78 of 120 Meeting"))
        records.add(PtaMeetingsRecord("End of Term PTA Meeting", "Active", "May 20, 2025", "4:00 PM to 6:00PM", "School Auditorium", "Discussion on student performance, upcoming events, and feedback on school policies.", "78 of 120 Meeting"))
        records.add(PtaMeetingsRecord("End of Term PTA Meeting", "Active", "May 20, 2025", "4:00 PM to 6:00PM", "School Auditorium", "Discussion on student performance, upcoming events, and feedback on school policies.", "78 of 120 Meeting"))
        records.add(PtaMeetingsRecord("End of Term PTA Meeting", "Active", "May 20, 2025", "4:00 PM to 6:00PM", "School Auditorium", "Discussion on student performance, upcoming events, and feedback on school policies.", "78 of 120 Meeting"))
        records.add(PtaMeetingsRecord("End of Term PTA Meeting", "Active", "May 20, 2025", "4:00 PM to 6:00PM", "School Auditorium", "Discussion on student performance, upcoming events, and feedback on school policies.", "78 of 120 Meeting"))
        records.add(PtaMeetingsRecord("End of Term PTA Meeting", "Active", "May 20, 2025", "4:00 PM to 6:00PM", "School Auditorium", "Discussion on student performance, upcoming events, and feedback on school policies.", "78 of 120 Meeting"))
        records.add(PtaMeetingsRecord("End of Term PTA Meeting", "Active", "May 20, 2025", "4:00 PM to 6:00PM", "School Auditorium", "Discussion on student performance, upcoming events, and feedback on school policies.", "78 of 120 Meeting"))
        records.add(PtaMeetingsRecord("End of Term PTA Meeting", "Active", "May 20, 2025", "4:00 PM to 6:00PM", "School Auditorium", "Discussion on student performance, upcoming events, and feedback on school policies.", "78 of 120 Meeting"))


        ptaMeetingsAdapter.setPtaMeetings(records)
    }

    data class PtaMeetingsRecord(val meetingTitle: String, val status: String, val meetingDate: String, val meetingTime: String, val meetingLocation: String, val meetingDescription: String, val tvAttendeesCount: String)

    private inner class PtaMeetingsAdapter :
        RecyclerView.Adapter<PtaMeetingsFragment.PtaMeetingsAdapter.ViewHolder>() {
        private var ptaMeetingsRecords = listOf<PtaMeetingsRecord>()

        fun setPtaMeetings(records: List<PtaMeetingsRecord>) {
            this.ptaMeetingsRecords = records
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_pta_meeting, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = ptaMeetingsRecords[position]
            holder.tvMeetingTitle.text = record.meetingTitle
            holder.chipStatus.text = record.status
            holder.tvMeetingDate.text = record.meetingDate
            holder.tvMeetingTime.text = record.meetingTime
            holder.tvMeetingLocation.text = record.meetingLocation
            holder.tvMeetingDescription.text = record.meetingDescription
            holder.tvAttendeesCount.text = record.tvAttendeesCount

        }

        override fun getItemCount() = ptaMeetingsRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvMeetingTitle: TextView = itemView.findViewById(R.id.tvMeetingTitle)
            val chipStatus: Chip = itemView.findViewById(R.id.chipStatus)
            val tvMeetingDate: TextView = itemView.findViewById(R.id.tvMeetingDate)
            val tvMeetingTime: TextView = itemView.findViewById(R.id.tvMeetingTime)
            val tvMeetingLocation: TextView = itemView.findViewById(R.id.tvMeetingLocation)
            val tvMeetingDescription: TextView = itemView.findViewById(R.id.tvMeetingDescription)
            val tvAttendeesCount: TextView = itemView.findViewById(R.id.tvAttendeesCount)
        }
    }


}