package com.example.schoolapp.adapters

import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.responses.SubjectMark
import com.google.android.material.progressindicator.LinearProgressIndicator


class SubjectAdapter(private val subjects: List<SubjectMark>, private val context: Context) : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = subjects[position]

        // ✅ Updated to use the correct properties from the SubjectMark data class
        holder.tvSubjectName.text = subject.subject ?: "N/A" // Use 'N/A' if the subject name is null
        holder.tvSubjectMarks.text = "Marks: ${subject.marks}/${subject.maxMarks}"

        // ✅ Safely convert the marks to integers and handle potential nulls
        val currentMarks = subject.marks.toIntOrNull() ?: 0
        val maxMarks = subject.maxMarks.toIntOrNull() ?: 100

        // ✅ Set the max value of the progress bar based on the subject's max marks
        holder.progressIndicatorSubject.max = maxMarks

        // ✅ Animate the progress from 0 to the current marks
        val animation = ObjectAnimator.ofInt(holder.progressIndicatorSubject, "progress", 0, currentMarks)
        animation.duration = 1000 // duration in ms (1 second)
        animation.interpolator = DecelerateInterpolator() // smooth slowing down
        animation.start()
    }

    override fun getItemCount(): Int = subjects.size

    inner class SubjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSubjectName: TextView = itemView.findViewById(R.id.tvSubjectName)
        val tvSubjectMarks: TextView = itemView.findViewById(R.id.tvSubjectMarks)
        val progressIndicatorSubject: LinearProgressIndicator = itemView.findViewById(R.id.progressIndicatorSubject)
    }
}