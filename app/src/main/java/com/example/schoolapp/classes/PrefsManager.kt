package com.example.schoolapp.classes

import android.content.Context
import android.content.SharedPreferences
import com.example.schoolapp.responses.LoginResponse
import com.google.gson.Gson
import kotlin.math.log
import androidx.core.content.edit
import com.example.schoolapp.responses.StudentDetailResponse

class PrefsManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }


    companion object {
        const val PREF_NAME = "MyPrefs"

        fun setSession(context: Context, flag: Boolean) {
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit() {
                putBoolean("isLoggedIn", flag)
            }
        }

        fun getSession(context: Context): Boolean {
            val sharedPreferences =
                context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean("isLoggedIn", false)
        }

        fun setUserInformation(context: Context, loginResponse: LoginResponse) {
            val gson = Gson()
            val userJsonInformationString = gson.toJson(loginResponse)
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit() {
                putString("userInformation", userJsonInformationString)
            }
        }

        fun getUserInformation(context: Context) : LoginResponse {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val jsonFromPrefs = sharedPreferences.getString("userInformation", null)
            val userInformationObject = gson.fromJson(jsonFromPrefs, LoginResponse::class.java)
            return userInformationObject
        }

        fun setUserDetailedInformation(context: Context, studentDetailResponse: StudentDetailResponse) {
            val gson = Gson()
            val userDetailedInformationString = gson.toJson(studentDetailResponse)
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
                putString("userDetailedInformation", userDetailedInformationString)
            }
        }

        fun getUserDetailedInformation(context: Context): StudentDetailResponse {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val jsonFromPrefs = sharedPreferences.getString("userDetailedInformation", null)
            val userDetailedInformationObject = gson.fromJson(jsonFromPrefs, StudentDetailResponse::class.java)
            return userDetailedInformationObject
        }

        fun setSectionId(context: Context, sectionId: String) {
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit() {
                putString("sectionId", sectionId)
            }
        }

        fun getSectionId(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString("sectionId", "-1") ?: "-1"
        }

    }
}