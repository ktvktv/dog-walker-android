package com.example.dogwalker

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log


class WalkerOrderActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walker_order)

        val alarmMgr = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }

        // Set the alarm to start at 8:30 a.m.
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }

        Log.d("WalkerOrderActivity", "Set alarm every one minute.\nTimesInMillis: ${calendar.timeInMillis}")
        alarmMgr?.set(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis + 10 * 1000,
            alarmIntent
        )

        AlarmManager.INTERVAL_HOUR
        Log.d("WalkerOrderActivity", "After Set alarm every one minute.\nTimesInMillis: ${calendar.timeInMillis + 60 * 1000}")
    }
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.i("AlarmReceiver", "Alarm fired!")
    }

}