package com.example.schoolapp.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.schoolapp.R
import com.example.schoolapp.databinding.FragmentAchievementsBinding


class AchievementsFragment : Fragment() {

    private lateinit var binding: FragmentAchievementsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAchievementsBinding.inflate(inflater, container, false)



        return binding.root
    }


}