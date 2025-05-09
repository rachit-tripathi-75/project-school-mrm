package com.example.schoolapp.classes

import android.animation.AnimatorInflater
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.schoolapp.R
import com.example.schoolapp.activities.AcademicCalendarActivity
import com.example.schoolapp.activities.AttendanceRegisterActivity
import com.example.schoolapp.activities.ExtraCurricularActivity
import com.example.schoolapp.activities.FeeDepositActivity
import com.example.schoolapp.activities.GrievanceActivity
import com.example.schoolapp.activities.HomeWorkActivity
import com.example.schoolapp.activities.LeaveRequestActivity
import com.example.schoolapp.activities.MyTimeTableActivity
import com.example.schoolapp.activities.NoticeBoardActivity
import com.example.schoolapp.activities.PtaMeetingActivity
import com.example.schoolapp.activities.ReportCardActivity
import com.example.schoolapp.activities.StudentCommentActivity
import com.example.schoolapp.activities.SubjectNotesActivity
import com.google.android.material.card.MaterialCardView

data class DashboardItem(val iconResId: Int, val label: String)

class DashboardAdapter(val context: Context, private val items: List<DashboardItem>) :
    RecyclerView.Adapter<DashboardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view as MaterialCardView
        val icon: ImageView = view.findViewById(R.id.icon)
        val label: TextView = view.findViewById(R.id.label)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_dashboard, parent, false)

        // Apply state list animators for elevation and scale
        val elevationAnimator = AnimatorInflater.loadStateListAnimator(
            parent.context,
            R.animator.card_elevation_animator
        )

        val scaleAnimator = AnimatorInflater.loadStateListAnimator(
            parent.context,
            R.animator.card_scale_animator
        )

        (view as MaterialCardView).stateListAnimator = elevationAnimator

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.iconResId)
        holder.label.text = item.label

        // Add click animation
        holder.card.setOnClickListener { view ->
            animateCardClick(view)
            when (position) {
                0 -> {
                    context.startActivity(Intent(context, NoticeBoardActivity::class.java))
                }
                1 -> {
                    context.startActivity(Intent(context, AttendanceRegisterActivity::class.java))
                }
                2 -> {
                    context.startActivity(Intent(context, MyTimeTableActivity::class.java))
                }
                3 -> {
                    context.startActivity(Intent(context, ReportCardActivity::class.java))
                }
                4 -> {
                    context.startActivity(Intent(context, FeeDepositActivity::class.java))
                }
                5 -> {
                    context.startActivity(Intent(context, SubjectNotesActivity::class.java))
                }
                6 -> {
                    context.startActivity(Intent(context, AcademicCalendarActivity::class.java))
                }
                7 -> {
                    context.startActivity(Intent(context, LeaveRequestActivity::class.java))
                }
                8 -> {
                    context.startActivity(Intent(context, PtaMeetingActivity::class.java))
                }
                9 -> {
                    context.startActivity(Intent(context, HomeWorkActivity::class.java))
                }
                10 -> {
                    context.startActivity(Intent(context, GrievanceActivity::class.java))
                }
                11 -> {
                    context.startActivity(Intent(context, ExtraCurricularActivity::class.java))
                }
                12 -> {
                    context.startActivity(Intent(context, StudentCommentActivity::class.java))
                }
            }
        }

    }

    private fun animateCardClick(view: View) {
        // Scale down
        view.animate()
            .scaleX(0.95f)
            .scaleY(0.95f)
            .setDuration(100)
            .withEndAction {
                // Scale back up with slight bounce
                view.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(200)
                    .setInterpolator(OvershootInterpolator(1.5f))
                    .start()
            }
            .start()
    }

    override fun getItemCount() = items.size
}