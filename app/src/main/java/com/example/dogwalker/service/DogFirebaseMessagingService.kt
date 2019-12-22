package com.example.dogwalker.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dogwalker.DashboardActivity
import com.example.dogwalker.R
import com.example.dogwalker.WalkerDashboardActivity


class DogFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = DogFirebaseMessagingService::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.size > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        var intent: Intent
        if(remoteMessage.data["From"] == "Walker") {
            intent = Intent(this, DashboardActivity::class.java)
            intent.putExtra("isNotified", 2)
        } else {
            intent = Intent(this, WalkerDashboardActivity::class.java)
            intent.putExtra("isFromNotify", true)
            intent.putExtra("photo", remoteMessage.data["photo"])
            intent.putExtra("description", remoteMessage.data["description"])
            intent.putExtra("date", remoteMessage.data["date"])
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)

        var builder = NotificationCompat.Builder(this, "2")
            .setSmallIcon(R.mipmap.dog)
            .setContentTitle("Test")
            .setContentText("Testing")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        createNotificationChannel()

        with(NotificationManagerCompat.from(this)) {
            // notificationId is a unique int for each notification that you must define
            notify(1, builder.build())
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "1"
            val descriptionText = ""
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("2", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onNewToken(p0: String) {
        Log.d(TAG, "New token: $p0")

        //TODO:Send Token to server
    }
}