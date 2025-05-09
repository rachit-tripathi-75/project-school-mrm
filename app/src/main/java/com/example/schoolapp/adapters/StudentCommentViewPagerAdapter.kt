package com.example.schoolapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.schoolapp.fragments.CommentsListFragment

class StudentCommentViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 1
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CommentsListFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
