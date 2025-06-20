package com.example.schoolapp.services

import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.schoolapp.ApplicationClass
import com.example.schoolapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

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
    }

}