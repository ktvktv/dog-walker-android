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
import com.example.dogwalker.viewmodel.InfoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class DogFirebaseMessagingService : FirebaseMessagingService() {

    private val TAG = DogFirebaseMessagingService::class.java.simpleName

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from!!)

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
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
            val isRating = remoteMessage.data["description"]!!.toLowerCase() == "rating"
            intent = Intent(this, DashboardActivity::class.java)
            if(isRating) {
                intent.putExtra("isRating", true)
            }
            intent.putExtra("isNotified", 2)
        } else {
            intent = Intent(this, WalkerDashboardActivity::class.java)
            intent.putExtra("isFromNotify", true)
            intent.putExtra("id", remoteMessage.data["id"])
            intent.putExtra("photo", remoteMessage.data["photo"])
            intent.putExtra("description", remoteMessage.data["description"])
            intent.putExtra("date", remoteMessage.data["date"])
            intent.putExtra("duration", remoteMessage.data["duration"])
            intent.putExtra("title", remoteMessage.notification!!.title)
            intent.putExtra("body", remoteMessage.notification!!.body)
        }
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var builder = NotificationCompat.Builder(this, "2")
            .setSmallIcon(R.mipmap.dog)
            .setContentTitle(remoteMessage.notification?.title)
            .setContentText(remoteMessage.notification?.body)
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

        val infoViewModel = InfoViewModel()
        val session = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        CoroutineScope(Job() + Dispatchers.IO).launch {
            infoViewModel.updateToken(session, p0)
        }
    }
}