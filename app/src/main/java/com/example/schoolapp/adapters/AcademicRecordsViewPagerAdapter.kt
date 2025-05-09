package com.example.schoolapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.schoolapp.fragments.AchievementsFragment
import com.example.schoolapp.fragments.AdditionalInfoFragment
import com.example.schoolapp.fragments.BasicInfoFragment
import com.example.schoolapp.fragments.GradesFragment
import com.example.schoolapp.fragments.ReportsFragment

class AcademicRecordsViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GradesFragment()
            1 -> ReportsFragment()
            2 -> AchievementsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
