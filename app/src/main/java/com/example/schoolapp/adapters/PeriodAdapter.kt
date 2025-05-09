package com.example.schoolapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.fragments.TimeTableFragment

class PeriodAdapter(
    private val context: Context,
    private val periods: List<TimeTableFragment.Period>,
    private var currentPeriodIndex: Int = -1
) : RecyclerView.Adapter<PeriodAdapter.PeriodViewHolder>() {

    class PeriodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val periodNumberText: TextView = view.findViewById(R.id.periodNumberText)
        val periodTimeText: TextView = view.findViewById(R.id.periodTimeText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeriodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.period_item, parent, false)
        return PeriodViewHolder(view)
    }

    override fun onBindViewHolder(holder: PeriodViewHolder, position: Int) {
        val period = periods[position]
        holder.periodNumberText.text = "${period.number}${getSuperscript(period.number)}"
        holder.periodTimeText.text = period.time

        // Highlight current period
        if (position == currentPeriodIndex) {
            holder.periodNumberText.setTextColor(ContextCompat.getColor(context, R.color.blue_600))
        } else {
            holder.periodNumberText.setTextColor(ContextCompat.getColor(context, R.color.gray_600))
        }

        // Apply animation
        val animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_right)
        animation.startOffset = (position * 50).toLong()
        holder.itemView.startAnimation(animation)
    }

    override fun getItemCount() = periods.size

    fun updateCurrentPeriod(newCurrentPeriodIndex: Int) {
        val oldCurrentPeriodIndex = currentPeriodIndex
        currentPeriodIndex = newCurrentPeriodIndex

        if (oldCurrentPeriodIndex != -1) {
            notifyItemChanged(oldCurrentPeriodIndex)
        }
        if (newCurrentPeriodIndex != -1) {
            notifyItemChanged(newCurrentPeriodIndex)
        }
    }

    private fun getSuperscript(number: Int): String {
        return when (number) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}