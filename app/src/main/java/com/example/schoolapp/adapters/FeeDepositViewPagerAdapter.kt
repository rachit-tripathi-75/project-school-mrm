package com.example.schoolapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.schoolapp.fragments.PayFeesFragment
import com.example.schoolapp.fragments.PaymentHistoryFragment

class FeeDepositViewPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PayFeesFragment()
            1 -> PaymentHistoryFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
