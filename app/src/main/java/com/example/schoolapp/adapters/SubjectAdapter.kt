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
import com.example.schoolapp.models.SubjectMark
import com.example.schoolapp.responses.GetMarksResponse
import com.example.schoolapp.responses.StudentMarkData
import com.google.android.material.progressindicator.LinearProgressIndicator

class SubjectAdapter(private val subjects: List<StudentMarkData>, private val context: Context, private val maxMarks: Int) : RecyclerView.Adapter<SubjectAdapter.SubjectViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubjectViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_subject, parent, false)
        return SubjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: SubjectViewHolder, position: Int) {
        val subject = subjects[position]
        holder.tvSubjectName.text = subject.exam_id
        holder.tvSubjectMarks.text = "Marks: ${subject.mark}/${maxMarks}"
        holder.progressIndicatorSubject.progress = subject.mark.toInt()
        val animation = ObjectAnimator.ofInt(holder.progressIndicatorSubject, "marksProgress", 0, subject.mark.toInt())
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
