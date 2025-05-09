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
import com.google.android.material.card.MaterialCardView
import com.google.android.material.chip.Chip

class ClassAdapter(private val context: Context, private val classes: List<TimeTableFragment.ClassInfo>,
    private var currentPeriodIndex: Int = -1
) : RecyclerView.Adapter<ClassAdapter.ClassViewHolder>() {

    class ClassViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val cardView: MaterialCardView = view as MaterialCardView
        val classNameText: TextView = view.findViewById(R.id.tvClassSubjectName)
        val classRoomText: TextView = view.findViewById(R.id.tvRoomNumber)
        val teacherChip: Chip = view.findViewById(R.id.chipTeacherName)
        val currentChip: Chip = view.findViewById(R.id.currentChip)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.class_item, parent, false)
        return ClassViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        val classInfo = classes[position]

        holder.classNameText.text = classInfo.name
        holder.classRoomText.text = classInfo.room
        holder.teacherChip.text = classInfo.teacher

        // Handle free period differently
        if (classInfo.name == "Free Period") {
            holder.classNameText.text = "Free Period"
            holder.classNameText.setTextColor(ContextCompat.getColor(context, R.color.gray_300))
            holder.classNameText.setTypeface(null, android.graphics.Typeface.ITALIC)
            holder.classRoomText.visibility = View.GONE
            holder.teacherChip.visibility = View.GONE
        } else {
            holder.classNameText.setTextColor(ContextCompat.getColor(context, R.color.blue_700))
            holder.classNameText.setTypeface(null, android.graphics.Typeface.BOLD)
            holder.classRoomText.visibility = View.VISIBLE
            holder.teacherChip.visibility = View.VISIBLE
        }

        // Highlight current class
        if (position == currentPeriodIndex) {
            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context, R.color.blue_50))
            holder.currentChip.visibility = View.VISIBLE
        } else {
            holder.cardView.setCardBackgroundColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.white
                )
            )
            holder.currentChip.visibility = View.GONE
        }

        // Apply animation
        val animation = AnimationUtils.loadAnimation(context, R.anim.item_animation_from_bottom)
        animation.startOffset = (position * 100).toLong()
        holder.itemView.startAnimation(animation)
    }

    override fun getItemCount() = classes.size

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
}