package com.example.schoolapp.activities

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.schoolapp.R
import com.example.schoolapp.adapters.ExtraCurricularActivitiesViewPagerAdapter
import com.example.schoolapp.adapters.StudentCommentViewPagerAdapter
import com.example.schoolapp.databinding.ActivityStudentCommentBinding

class StudentCommentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStudentCommentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityStudentCommentBinding.inflate(layoutInflater)
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

        binding.viewPager.adapter = StudentCommentViewPagerAdapter(this)
    }
}