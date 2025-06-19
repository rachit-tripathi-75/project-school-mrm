package com.example.schoolapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.models.AttendanceRecordModel

class AttendanceAdapter : RecyclerView.Adapter<AttendanceAdapter.ViewHolder>() {
    private var attendanceRecords = listOf<AttendanceRecordModel>()

    fun setAttendanceRecords(records: List<AttendanceRecordModel>) {
        this.attendanceRecords = records
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_attendance, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val record = attendanceRecords[position]
        holder.tvDate.text = record.date
        holder.tvStatus.text = record.status

        if (record.status == "1") {
            holder.tvStatus.setBackgroundResource(R.drawable.status_present_background)
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.status_absent_background)
        }
    }

    override fun getItemCount() = attendanceRecords.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }
}