package com.example.schoolapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import com.example.schoolapp.classes.FcmAuthToken
import com.google.firebase.messaging.FirebaseMessaging
import java.util.concurrent.Executors

class ApplicationClass : Application() {

    companion object {
        val FCM_CHANNEL_ID_NEW_INFO = "NEW_INFO_NOTIFICATION_CHANNEL";
    }

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val newBookingNotificationChannel = NotificationChannel(FCM_CHANNEL_ID_NEW_INFO, "New Info", NotificationManager.IMPORTANCE_HIGH)
            val newBookingNotificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            newBookingNotificationManager.createNotificationChannel(newBookingNotificationChannel)


        }

        val executorService = Executors.newSingleThreadExecutor()
        executorService.execute(Runnable {
            try {
                val token: String? = FcmAuthToken.getAccessToken(this)
                Log.d("Oauth2TokenTAG", "Oauth2.0 Token: " + token)
            } catch (e: Exception) {
                throw RuntimeException(e)
            }
        })

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("fcmTokenTAG", "FCM Token: " + token)
        }.addOnFailureListener { e->
            Log.d("fcmTokenTAG", "Error: " + e.message)
        }


    }


}

