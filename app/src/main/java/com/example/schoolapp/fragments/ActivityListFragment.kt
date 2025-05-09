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
import com.example.schoolapp.databinding.FragmentActivityListBinding
import com.google.android.material.chip.Chip


class ActivityListFragment : Fragment() {

    private lateinit var binding: FragmentActivityListBinding
    private lateinit var grievanceListAdapter: ActivityListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentActivityListBinding.inflate(inflater, container, false)


        initialisers()

        return binding.root
    }

    private fun initialisers() {
        setGrievanceRecyclerView()
        loadGrievanceData()
    }

    private fun setGrievanceRecyclerView() {

        binding.activitiesRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        grievanceListAdapter = ActivityListAdapter()
        binding.activitiesRecyclerView.adapter = grievanceListAdapter
    }

    private fun loadGrievanceData() {
        // loaded data from a database or API........

        val records = mutableListOf<ActivityRecord>()


        // Add some dummy data for other PTA Meetings......
        records.add(ActivityRecord("Enrolled", "Basketball Club", "Sports", "Every Tuesday and Thursday", "4:00 PM to 5:30 PM", "School Basketball Court", "Join our basketball club to improve your skills, fitness, and teamwork. Open to all skill levels.", "15/20"))
        records.add(ActivityRecord("Enrolled", "Basketball Club", "Sports", "Every Tuesday and Thursday", "4:00 PM to 5:30 PM", "School Basketball Court", "Join our basketball club to improve your skills, fitness, and teamwork. Open to all skill levels.", "15/20"))
        records.add(ActivityRecord("Enrolled", "Basketball Club", "Sports", "Every Tuesday and Thursday", "4:00 PM to 5:30 PM", "School Basketball Court", "Join our basketball club to improve your skills, fitness, and teamwork. Open to all skill levels.", "15/20"))
        records.add(ActivityRecord("Enrolled", "Basketball Club", "Sports", "Every Tuesday and Thursday", "4:00 PM to 5:30 PM", "School Basketball Court", "Join our basketball club to improve your skills, fitness, and teamwork. Open to all skill levels.", "15/20"))
        records.add(ActivityRecord("Enrolled", "Basketball Club", "Sports", "Every Tuesday and Thursday", "4:00 PM to 5:30 PM", "School Basketball Court", "Join our basketball club to improve your skills, fitness, and teamwork. Open to all skill levels.", "15/20"))
        records.add(ActivityRecord("Enrolled", "Basketball Club", "Sports", "Every Tuesday and Thursday", "4:00 PM to 5:30 PM", "School Basketball Court", "Join our basketball club to improve your skills, fitness, and teamwork. Open to all skill levels.", "15/20"))
        records.add(ActivityRecord("Enrolled", "Basketball Club", "Sports", "Every Tuesday and Thursday", "4:00 PM to 5:30 PM", "School Basketball Court", "Join our basketball club to improve your skills, fitness, and teamwork. Open to all skill levels.", "15/20"))


        grievanceListAdapter.setPtaMeetings(records)
    }

    data class ActivityRecord(val hasEnrolled: String, val activityTitle: String, val category: String, val activityDate: String, val activityTime: String, val activityLocation: String, val activityDescription: String, val enrollmentCount: String)

    private inner class ActivityListAdapter :
        RecyclerView.Adapter<ActivityListFragment.ActivityListAdapter.ViewHolder>() {
        private var activityRecords = listOf<ActivityRecord>()

        fun setPtaMeetings(records: List<ActivityRecord>) {
            this.activityRecords = records
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_activity, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = activityRecords[position]
            holder.chipEnrolledBadge.text = record.hasEnrolled
            holder.tvActivityTitle.text = record.activityTitle
            holder.chipCategory.text = record.category
            holder.tvActivityDate.text = record.activityDate
            holder.tvActivityTime.text = record.activityTime
            holder.tvActivityLocation.text = record.activityLocation
            holder.tvActivityDescription.text = record.activityDescription
            holder.tvEnrollmentCount.text = record.enrollmentCount

        }

        override fun getItemCount() = activityRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val chipEnrolledBadge: Chip = itemView.findViewById(R.id.chipEnrolledBadge)
            val tvActivityTitle: TextView = itemView.findViewById(R.id.tvActivityTitle)
            val chipCategory: Chip = itemView.findViewById(R.id.chipCategory)
            val tvActivityDate: TextView = itemView.findViewById(R.id.tvActivityDate)
            val tvActivityTime: TextView = itemView.findViewById(R.id.tvActivityTime)
            val tvActivityLocation: TextView = itemView.findViewById(R.id.tvActivityLocation)
            val tvActivityDescription: TextView = itemView.findViewById(R.id.tvActivityDescription)
            val tvEnrollmentCount: TextView = itemView.findViewById(R.id.tvEnrollmentCount)
        }
    }




}