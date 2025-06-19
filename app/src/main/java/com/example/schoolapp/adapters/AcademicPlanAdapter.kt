package com.example.schoolapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.responses.AcademicPlan

class AcademicPlanAdapter(
    private var academicPlans: List<AcademicPlan>,
    private val onItemClick: (AcademicPlan) -> Unit
) : RecyclerView.Adapter<AcademicPlanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_academic_plan, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(academicPlans[position])
    }

    override fun getItemCount(): Int = academicPlans.size

    fun updateData(newPlans: List<AcademicPlan>) {
        academicPlans = newPlans
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewTitle: TextView = itemView.findViewById(R.id.textViewTitle)
        private val textViewStatus: TextView = itemView.findViewById(R.id.textViewStatus)
        private val textViewDateRange: TextView = itemView.findViewById(R.id.textViewDateRange)
        private val textViewCreatedDate: TextView = itemView.findViewById(R.id.textViewCreatedDate)
        private val layoutAnnouncementNotice: LinearLayout = itemView.findViewById(R.id.layoutAnnouncementNotice)
        private val buttonViewDetails: Button = itemView.findViewById(R.id.buttonViewDetails)

        fun bind(academicPlan: AcademicPlan) {
            textViewTitle.text = academicPlan.title
            textViewDateRange.text = academicPlan.getFormattedDateRange()
            textViewCreatedDate.text = academicPlan.getFormattedCreatedDate()

            // Set status
            if (academicPlan.isActive()) {
                textViewStatus.text = "Active"
                textViewStatus.setBackgroundResource(R.drawable.badge_active)
                textViewStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.white))
            } else {
                textViewStatus.text = "Inactive"
                textViewStatus.setBackgroundResource(R.drawable.badge_inactive)
                textViewStatus.setTextColor(ContextCompat.getColor(itemView.context, R.color.text_secondary))
            }

            // Show/hide announcement notice
            layoutAnnouncementNotice.visibility =
                if (academicPlan.isAnnouncedLater()) View.VISIBLE else View.GONE

            // Set click listeners
            itemView.setOnClickListener { onItemClick(academicPlan) }
            buttonViewDetails.setOnClickListener { onItemClick(academicPlan) }
        }
    }
}