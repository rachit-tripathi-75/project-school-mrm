package com.example.schoolapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.databinding.ItemLeaveRequestBinding
import com.example.schoolapp.models.LeaveRequestModel

class LeaveRequestAdapter(
    private var leaveRequests: List<LeaveRequestModel>
) : RecyclerView.Adapter<LeaveRequestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLeaveRequestBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(leaveRequests[position])
    }

    override fun getItemCount(): Int = leaveRequests.size

    fun updateList(newList: List<LeaveRequestModel>) {
        leaveRequests = newList
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemLeaveRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(request: LeaveRequestModel) {
            binding.apply {
                tvReason.text = request.reason
                tvLeaveType.text = request.leaveType
                tvDateRange.text = "${request.startDate} to ${request.endDate}"
                tvDescription.text = request.description

                when (request.approveStatus) {
                    "0" -> { // pending
                        ivStatusIcon.setImageResource(R.drawable.ic_pending)
                        ivStatusIcon.setColorFilter(
                            ContextCompat.getColor(itemView.context, R.color.yellow_600)
                        )
                        tvStatus.background = ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.status_badge_pending
                        )
                        tvStatus.text = "Pending"
                        tvSubmittedDate.text = request.createdAt
                    }
                    "1" -> { // approved
                        ivStatusIcon.setImageResource(R.drawable.ic_check_circle)
                        ivStatusIcon.setColorFilter(
                            ContextCompat.getColor(itemView.context, R.color.green_600)
                        )
                        tvStatus.background = ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.status_badge_accepted
                        )
                        tvStatus.text = "Approved"
                        tvSubmittedDate.text = request.createdAt
                    }
                    "-1" -> { // rejected
                        ivStatusIcon.setImageResource(R.drawable.ic_cancel)
                        ivStatusIcon.setColorFilter(
                            ContextCompat.getColor(itemView.context, R.color.red_600)
                        )
                        tvStatus.background = ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.status_badge_rejected
                        )
                        tvStatus.text = "Rejected"
                        tvSubmittedDate.text = request.createdAt
                    }
                }
            }
        }
    }
}
