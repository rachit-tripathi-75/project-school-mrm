package com.example.schoolapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.databinding.ItemSubjectNotesBinding
import com.example.schoolapp.models.SubjectNotesModel

class SubjectNotesAdapter(
    private val onActionClick: (SubjectNotesModel, Action) -> Unit
) : RecyclerView.Adapter<SubjectNotesAdapter.HomeworkViewHolder>() {

    enum class Action { VIEW, DOWNLOAD }

    private val notesList = mutableListOf<SubjectNotesModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeworkViewHolder {
        val binding = ItemSubjectNotesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HomeworkViewHolder(binding)
    }

    override fun getItemCount(): Int = notesList.size

    override fun onBindViewHolder(holder: HomeworkViewHolder, position: Int) {
        holder.bind(notesList[position])
    }

    inner class HomeworkViewHolder(
        private val binding: ItemSubjectNotesBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(homework: SubjectNotesModel) {
            binding.apply {
                textViewTitle.text = homework.title
                chipSubject.text = homework.subject
                textViewId.text = "#${homework.id}"
                textViewDescription.text = homework.description
                textViewDueDate.text = "Due: ${homework.dueDate}"
                textViewSubjectId.text = "Subject ID: ${homework.subId}"
                textViewSection.text = "Section: ${homework.secId}"
                textViewSession.text = "Session: ${homework.sessionId}"

                // Set subject chip color
                val chipColor = when (homework.subject) {
                    "Mathematics" -> R.color.math_chip_bg
                    "Physics" -> R.color.physics_chip_bg
                    "Chemistry" -> R.color.chemistry_chip_bg
                    "Biology" -> R.color.biology_chip_bg
                    "English" -> R.color.english_chip_bg
                    else -> R.color.default_chip_bg
                }
                chipSubject.setChipBackgroundColorResource(chipColor)

                buttonViewPdf.setOnClickListener {
                    onActionClick(homework, Action.VIEW)
                }

                btnDownloadPdf.setOnClickListener {
                    onActionClick(homework, Action.DOWNLOAD)
                }
            }
        }
    }

    fun setData(newList: List<SubjectNotesModel>) {
        notesList.clear()
        notesList.addAll(newList)
        notifyDataSetChanged()
    }
}
