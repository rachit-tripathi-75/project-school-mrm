package com.example.schoolapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.schoolapp.databinding.FragmentLeaveRequestListBinding


class LeaveRequestListFragment : Fragment() {
    private lateinit var binding: FragmentLeaveRequestListBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLeaveRequestListBinding.inflate(inflater, container, false)



        return binding.root
    }

}