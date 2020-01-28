package com.example.dogwalker.receiver

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.dogwalker.R
import com.example.dogwalker.WalkerDashboardActivity
import com.example.dogwalker.data.TransactionStatus
import com.example.dogwalker.network.DogWalkerServiceApi
import com.example.dogwalker.viewmodel.OngoingOrderViewModel
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.*

class AlarmReceiver : BroadcastReceiver() {
    private val TAG = AlarmReceiver::class.java.simpleName

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(TAG, "Before Notification")

        val id = intent!!.extras.getInt("id")

        var title = intent.extras.getString("title")
        var body = intent.extras.getString("body")

        if(title == "") {
            title = "Waktunya menjalankan anjing!"
        }

        if(body == "") {
            body = "Jangan lupa menjalankan anjing."
        }

        val intent = Intent(context, WalkerDashboardActivity::class.java)
        intent.putExtra("isFromNotify", false)

        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        var builder = NotificationCompat.Builder(context!!, "2")
            .setSmallIcon(R.mipmap.dog)
            .setContentTitle(title)
            .setContentText(body)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(2, builder.build())
        }

        val sharedPreferences = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        val session = sharedPreferences.getString(context.getString(R.string.session_cache), "")

        CoroutineScope(Job() + Dispatchers.Main).launch {
            val resp = DogWalkerServiceApi.DogWalkerService.changeTransactionStatus(session, TransactionStatus(
                    id, "berlangsung"
                ))!!.await()

            Log.d(TAG, "$resp")
        }

        val phone = sharedPreferences.getString(context.getString(R.string.phone_number_cache), "")
        val path = "walker/$phone/done"
        val ref = FirebaseDatabase.getInstance().getReference(path)
        ref.setValue("No")

        Log.i(TAG, "Alarm fired!")
    }
}