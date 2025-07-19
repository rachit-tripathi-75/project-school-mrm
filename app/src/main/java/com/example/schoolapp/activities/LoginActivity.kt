package com.example.schoolapp.activities

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.schoolapp.MainActivity
import com.example.schoolapp.R
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.ApiServices
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.databinding.ActivityLoginBinding
import com.example.schoolapp.requests.UpdateFcmTokenRequest
import com.example.schoolapp.responses.LoginResponse
import com.example.schoolapp.responses.StudentDetailResponse
import com.example.schoolapp.responses.UpdateFcmTokenResponse
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.math.log

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var isPasswordVisible: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        listeners()
    }

    private fun listeners() {
        binding.btnSignIn.setOnClickListener {
            if (isValidDetails()) {
                signIn()
            }
        }
        binding.togglePasswordButton.setOnClickListener { view ->
            if (isPasswordVisible) {
                // Hide password
                binding.etPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.togglePasswordButton.setImageResource(R.drawable.ic_visibility_off)
            } else {
                // Show password
                binding.etPassword.transformationMethod =
                    HideReturnsTransformationMethod.getInstance()
                binding.togglePasswordButton.setImageResource(R.drawable.ic_visibility)
            }
            isPasswordVisible = !isPasswordVisible
            binding.etPassword.setSelection(binding.etPassword.text.length)
        }

    }

    private fun isValidDetails(): Boolean {
        if (binding.etEnrollmentNumber.text.toString().isEmpty()) {
            binding.etEnrollmentNumber.setError("Enter enrollment number")
            return false
        } else if (binding.etPassword.text.toString().isEmpty()) {
            binding.etPassword.setError("Enter password")
            return false
        }
        return true
    }

    private fun signIn() {
        binding.btnSignIn.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        ApiClient.loginInstance.login(binding.etEnrollmentNumber.text.toString(), binding.etPassword.text.toString()).enqueue(object : retrofit2.Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: retrofit2.Response<LoginResponse>) {
                binding.btnSignIn.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d("loginResponseTAG", response.body()?.Msg.toString())
                    val loginData = response.body()
                    if (loginData?.status == 1) {
                        fetchStudentInformation()
                        PrefsManager.setUserInformation(this@LoginActivity, loginData)
                        PrefsManager.setSession(this@LoginActivity, true)
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        storeFCMTokenToServer()
                        finish()
                    }

                } else {
                    Log.d("loginResponseTAG", response.body()?.Msg.toString())
                    Toast.makeText(this@LoginActivity, "Incorrect Credentials", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                binding.btnSignIn.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                Log.d("loginResponseTAG", "Error: ${t.message}")
                Toast.makeText(this@LoginActivity, "An error has occurred. Please try again later", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun storeFCMTokenToServer() {

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            val s = UpdateFcmTokenRequest(binding.etEnrollmentNumber.text.toString(), token)
            ApiClient.updateFcmTokenInstance.updateFcmToken(
                "application/json",
                "ci_session=iud5psvipvp7npbc74oi9thgkbaoq0m0",
                s
            ).enqueue(object : retrofit2.Callback<UpdateFcmTokenResponse> {
                override fun onResponse(call: Call<UpdateFcmTokenResponse>, response: retrofit2.Response<UpdateFcmTokenResponse>) {
                    if (response.isSuccessful) {
                        Log.d("storeFCMTokenToServerTAG", response.body()?.Msg!!)
                    }
                }

                override fun onFailure(call: Call<UpdateFcmTokenResponse>, t: Throwable) {
                    Log.d("storeFCMTokenToServerTAG", "onFailure: " + t.message)
                }
            })
        }



    }

    private fun fetchStudentInformation() {

        ApiClient.getStudentDetailInstance.getStudentDetail(
            "Bearer 8a56598bd5114ab31f6f70e76e1873e8945eafcd915b3f6ada4c0132d212a57e",
            "ci_session=9muik1cd5884084kjfnq9vo3d3k5fhfu",
            binding.etEnrollmentNumber.text.toString()
        ).enqueue(object : retrofit2.Callback<StudentDetailResponse> {

            override fun onResponse(call: Call<StudentDetailResponse>, response: Response<StudentDetailResponse>) {
                binding.btnSignIn.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.d("studentDetailTAG", "onResponse(), If--> Message: " + response.body()!!.message + "Status: " + response.body()!!.status)
                    val s = response.body()
                    if (s?.status == 1) {
                        PrefsManager.setSectionId(this@LoginActivity, s.studentData[0].sectionId)
                        PrefsManager.setUserDetailedInformation(this@LoginActivity, s)
                    }

                } else {
                    Log.d("studentDetailTAG", "onResponse, else--> Message: " + response.body()!!.message + "Status: " + response.body()!!.status)
                    PrefsManager.setSectionId(this@LoginActivity, "-1")
                }
            }

            override fun onFailure(call: Call<StudentDetailResponse>, t: Throwable) {
                PrefsManager.setSectionId(this@LoginActivity, "-1")
                Log.d("studentDetailTAG", "Error: ${t.message}")
            }
        })

    }


}