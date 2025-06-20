package com.example.schoolapp.classes

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.messaging.FirebaseMessaging

class FcmAuthToken {
    companion object {
        @Throws(Exception::class)
        fun getAccessToken(context: Context): String {
            val stream = context.getAssets().open("schoolapp-d8e13-firebase-adminsdk-fbsvc-116b4828c5.json")
            val googleCredentials: GoogleCredentials = GoogleCredentials.fromStream(stream)
                .createScoped(mutableListOf("https://www.googleapis.com/auth/firebase.messaging"))

            googleCredentials.refreshIfExpired()
            return googleCredentials.getAccessToken().getTokenValue()
        }
    }


}