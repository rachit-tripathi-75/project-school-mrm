package com.example.schoolapp.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.schoolapp.R
import com.example.schoolapp.adapters.AcademicRecordsViewPagerAdapter
import com.example.schoolapp.databinding.ActivityAcademicRecordsBinding
import com.google.android.material.tabs.TabLayoutMediator

class AcademicRecordsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAcademicRecordsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAcademicRecordsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()
        listeners()

    }

    private fun initialisers() {
        binding.viewPager.adapter = AcademicRecordsViewPagerAdapter(this)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Grades"
                1 -> "Reports"
                2 -> "Achievements"
                else -> ""
            }
        }.attach()
    }

    private fun listeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }
    }



}