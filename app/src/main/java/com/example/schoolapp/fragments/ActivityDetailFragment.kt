package com.example.schoolapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.example.schoolapp.R
import com.example.schoolapp.databinding.FragmentActivityDetailBinding


class ActivityDetailFragment : Fragment() {

    private lateinit var binding: FragmentActivityDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentActivityDetailBinding.inflate(inflater, container, false)



        return binding.root
    }
}