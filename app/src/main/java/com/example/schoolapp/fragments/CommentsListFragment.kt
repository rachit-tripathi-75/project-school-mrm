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
import com.example.schoolapp.databinding.FragmentCommentsListBinding
import com.google.android.material.chip.Chip
import com.google.android.material.textfield.TextInputEditText


class CommentsListFragment : Fragment() {

    private lateinit var binding: FragmentCommentsListBinding
    private lateinit var commentsListAdapter: CommentsListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentCommentsListBinding.inflate(inflater, container, false)

        initialisers()

        return binding.root
    }

    private fun initialisers() {
        setGrievanceRecyclerView()
        loadGrievanceData()
    }

    private fun setGrievanceRecyclerView() {

        binding.commentsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        commentsListAdapter = CommentsListAdapter()
        binding.commentsRecyclerView.adapter = commentsListAdapter
    }

    private fun loadGrievanceData() {
        // loaded data from a database or API........

        val records = mutableListOf<CommentListRecord>()


        // Add some dummy data for other PTA Meetings......
        records.add(CommentListRecord("Alex Johnson", "2 hours ago", "Suggestions", "I think we should organize a science fair next month. It would be a great opportunity for students to showcase their projects and learn from each other.", "15", "2", "How many will be the members?"))
        records.add(CommentListRecord("Alex Johnson", "2 hours ago", "Suggestions", "I think we should organize a science fair next month. It would be a great opportunity for students to showcase their projects and learn from each other.", "15", "2", "How many will be the members?"))
        records.add(CommentListRecord("Alex Johnson", "2 hours ago", "Suggestions", "I think we should organize a science fair next month. It would be a great opportunity for students to showcase their projects and learn from each other.", "15", "2", "How many will be the members?"))
        records.add(CommentListRecord("Alex Johnson", "2 hours ago", "Suggestions", "I think we should organize a science fair next month. It would be a great opportunity for students to showcase their projects and learn from each other.", "15", "2", "How many will be the members?"))
        records.add(CommentListRecord("Alex Johnson", "2 hours ago", "Suggestions", "I think we should organize a science fair next month. It would be a great opportunity for students to showcase their projects and learn from each other.", "15", "2", "How many will be the members?"))
        records.add(CommentListRecord("Alex Johnson", "2 hours ago", "Suggestions", "I think we should organize a science fair next month. It would be a great opportunity for students to showcase their projects and learn from each other.", "15", "2", "How many will be the members?"))



        commentsListAdapter.setPtaMeetings(records)
    }

    data class CommentListRecord(val authorName: String, val commentDate: String, val category: String, val comment: String, val likeCount: String, val dislikeCount: String, val reply: String)

    private inner class CommentsListAdapter :
        RecyclerView.Adapter<CommentsListFragment.CommentsListAdapter.ViewHolder>() {
        private var commentsListsRecords = listOf<CommentListRecord>()

        fun setPtaMeetings(records: List<CommentListRecord>) {
            this.commentsListsRecords = records
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_comment, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val record = commentsListsRecords[position]
            holder.tvAuthorName.text = record.authorName
            holder.tvCommentDate.text = record.commentDate
            holder.category.text = record.category
            holder.tvComment.text = record.comment
            holder.tvLikeCount.text = record.likeCount
            holder.tvDislikeCount.text = record.dislikeCount

        }

        override fun getItemCount() = commentsListsRecords.size

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val tvAuthorName: TextView = itemView.findViewById(R.id.tvAuthorName)
            val tvCommentDate: TextView = itemView.findViewById(R.id.tvCommentDate)
            val category: Chip = itemView.findViewById(R.id.chipCategory)
            val tvComment: TextView = itemView.findViewById(R.id.tvComment)
            val tvLikeCount: TextView = itemView.findViewById(R.id.tvLikeCount)
            val tvDislikeCount: TextView = itemView.findViewById(R.id.tvDislikeCount)

        }
    }





}