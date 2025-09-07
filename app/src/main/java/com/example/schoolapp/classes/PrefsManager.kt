package com.example.schoolapp.classes

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.schoolapp.responses.LoginResponse
import com.example.schoolapp.responses.StudentDetailResponse
import com.google.gson.Gson

class PrefsManager(private val context: Context) {
    private val sharedPreferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        editor = sharedPreferences.edit()
    }

    companion object {
        const val PREF_NAME = "MyPrefs"
        private const val KEY_USER_INFO = "userInformation"
        private const val KEY_USER_DETAILED_INFO = "userDetailedInformation"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_SECTION_ID = "sectionId"

        fun setSession(context: Context, flag: Boolean) {
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
                putBoolean(KEY_IS_LOGGED_IN, flag)
            }
        }

        fun getSession(context: Context): Boolean {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)
        }

        fun setUserInformation(context: Context, loginResponse: LoginResponse) {
            val gson = Gson()
            val userJsonInformationString = gson.toJson(loginResponse)
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
                putString(KEY_USER_INFO, userJsonInformationString)
            }
        }

        // ✅ Corrected return type to be nullable (LoginResponse?)
        fun getUserInformation(context: Context): LoginResponse? {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val jsonFromPrefs = sharedPreferences.getString(KEY_USER_INFO, null)
            return if (jsonFromPrefs != null) {
                gson.fromJson(jsonFromPrefs, LoginResponse::class.java)
            } else {
                null
            }
        }

        fun setUserDetailedInformation(context: Context, studentDetailResponse: StudentDetailResponse) {
            val gson = Gson()
            val userDetailedInformationString = gson.toJson(studentDetailResponse)
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
                putString(KEY_USER_DETAILED_INFO, userDetailedInformationString)
            }
        }

        // ✅ Corrected return type to be nullable (StudentDetailResponse?)
        fun getUserDetailedInformation(context: Context): StudentDetailResponse? {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            val gson = Gson()
            val jsonFromPrefs = sharedPreferences.getString(KEY_USER_DETAILED_INFO, null)
            return if (jsonFromPrefs != null) {
                gson.fromJson(jsonFromPrefs, StudentDetailResponse::class.java)
            } else {
                null
            }
        }

        fun setSectionId(context: Context, sectionId: String) {
            context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE).edit {
                putString(KEY_SECTION_ID, sectionId)
            }
        }

        fun getSectionId(context: Context): String {
            val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            return sharedPreferences.getString(KEY_SECTION_ID, "-1") ?: "-1"
        }
    }
}