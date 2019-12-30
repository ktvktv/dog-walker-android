package com.example.dogwalker.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dogwalker.R
import com.example.dogwalker.WalkerDashboardActivity
import com.example.dogwalker.data.TransactionStatus
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.*

class AlarmReceiver : BroadcastReceiver() {
    private val TAG = AlarmReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Before Notification")

        val id = intent!!.extras.getInt("id")

        var title = intent.extras.getString("title")
        var body = intent.extras.getString("body")

        if(title == "") {
            title = "Time to walk the dog"
        }

        if(body == "") {
            body = "Walk the dog now sir!"
        }

        var builder = NotificationCompat.Builder(context!!, "2")
            .setSmallIcon(R.mipmap.dog)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(2, builder.build())
        }

        val session = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(context.getString(R.string.session_cache), "")

        CoroutineScope(Job() + Dispatchers.Main).launch {
            val resp = DogWalkerServiceApi.DogWalkerService.changeTransactionStatus(session, TransactionStatus(
                    id, "ONGOING"
                ))!!.await()

            Log.d(TAG, "$resp")
        }

        Log.i(TAG, "Alarm fired!")
    }
}