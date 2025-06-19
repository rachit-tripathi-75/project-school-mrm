package com.example.schoolapp.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.schoolapp.R
import com.example.schoolapp.adapters.PtaMeetingViewPagerAdapter
import com.example.schoolapp.databinding.ActivityPtaMeetingBinding

class PtaMeetingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPtaMeetingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPtaMeetingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initialisers()

    }

    private fun initialisers() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.viewPager.adapter = PtaMeetingViewPagerAdapter(this)
    }



}