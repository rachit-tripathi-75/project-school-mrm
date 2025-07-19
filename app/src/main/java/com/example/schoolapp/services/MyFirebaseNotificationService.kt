package com.example.schoolapp.services

import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.schoolapp.ApplicationClass
import com.example.schoolapp.R
import com.example.schoolapp.classes.ApiClient
import com.example.schoolapp.classes.PrefsManager
import com.example.schoolapp.requests.UpdateFcmTokenRequest
import com.example.schoolapp.responses.UpdateFcmTokenResponse
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import retrofit2.Call

class MyFirebaseNotificationService : FirebaseMessagingService() {

    private var data1: String = ""
    private var data2: String = ""

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        if (remoteMessage.notification != null) {
            // leaving it for announcements etc, which doesn't requires user's interaction on notification tray.....
        }

        if (remoteMessage.data.isNotEmpty()) {
            data1 = remoteMessage.data.get("data1").toString()
            data2 = remoteMessage.data.get("data2").toString()
            sendNotification()
        }

    }

    private fun sendNotification() {


        // building notification tray, for a specific channel.........
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(this, ApplicationClass.FCM_CHANNEL_ID_NEW_INFO)
                .setSmallIcon(R.drawable.ashokpublicschoollogo)
                .setContentTitle(data1)
                .setContentText(data2)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // to display heads-up notificaton.......
        notificationManager.notify(0, notificationBuilder.build())
        Log.d("blockname", "insideBhajanMandaliElseBlock")
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // api to store the generated token in the server.......

        val s = UpdateFcmTokenRequest(PrefsManager.getUserDetailedInformation(this).studentData.get(0).enrollment, token)
        ApiClient.updateFcmTokenInstance.updateFcmToken(
            "application/json",
            "ci_session=iud5psvipvp7npbc74oi9thgkbaoq0m0",
            s
        ).enqueue(object : retrofit2.Callback<UpdateFcmTokenResponse> {
            override fun onResponse(
                call: Call<UpdateFcmTokenResponse>,
                response: retrofit2.Response<UpdateFcmTokenResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("onNewTokenTAG", response.body()?.Msg!!)
                }
            }

            override fun onFailure(call: Call<UpdateFcmTokenResponse>, t: Throwable) {
                Log.d("onNewTokenTAG", "onFailure: " + t.message)
            }
        })


    }

}