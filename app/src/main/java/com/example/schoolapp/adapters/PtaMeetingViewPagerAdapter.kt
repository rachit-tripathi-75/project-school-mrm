package com.example.schoolapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.schoolapp.fragments.PtaMeetingsFragment

class PtaMeetingViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 1
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PtaMeetingsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
