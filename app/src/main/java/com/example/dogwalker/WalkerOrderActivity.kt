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
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.dogwalker.view.ListOrderFragment
import com.example.dogwalker.view.WalkerOrderFragment


class WalkerOrderActivity: AppCompatActivity() {
    val TAG = WalkerOrderActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_walker_order)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        val isUpAllowed = findNavController(R.id.walker_order_host).navigateUp()

        if(!isUpAllowed) {
            super.onBackPressed()
        }
    }
}