package com.example.schoolapp.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.schoolapp.R
import com.example.schoolapp.databinding.ActivityMyAccountBinding

class MyAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMyAccountBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMyAccountBinding.inflate(layoutInflater)
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

    }

    private fun listeners() {
        binding.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.cvPersonalInformation.setOnClickListener {
            startActivity(Intent(this@MyAccountActivity, PersonalInformationActivity::class.java))
        }

        binding.cvAcademicRecords.setOnClickListener {
            startActivity(Intent(this@MyAccountActivity, AcademicRecordsActivity::class.java))
        }

        binding.cvPriacyAndSecurity.setOnClickListener {
            startActivity(Intent(this@MyAccountActivity, PrivacyAndSecurityActivity::class.java))
        }

        binding.cvSettings.setOnClickListener {
            startActivity(Intent(this@MyAccountActivity, SettingsActivity::class.java))

        }

        binding.cvLogout.setOnClickListener {
            startActivity(Intent(this@MyAccountActivity, LoginActivity::class.java))
            finishAffinity()
        }

    }

}