package com.example.dogwalker.service

import android.Manifest
import android.app.*
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import android.content.IntentFilter
import com.example.dogwalker.R
import android.content.BroadcastReceiver
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.example.dogwalker.MapsActivity
import com.example.dogwalker.data.Location
import com.google.android.gms.location.*

class DogWalkerService : Service() {

    val TAG = DogWalkerService::class.java.simpleName

    val channelID = "tracker-channel-id"
    val channelName = "Tracker Channel"

    val TRACKER_NOTIFICATION_ID = 1

    private var locationCallBack: LocationCallback? = null
    private lateinit var client: FusedLocationProviderClient
    private lateinit var phone: String

    protected var stopReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "received stop broadcast")

            // Stop the service when the notification is tapped
            unregisterReceiver(this)
            stopSelf()
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(locationCallBack == null) {
            phone = intent!!.extras.get(MapsActivity.PHONE_EXTRA) as String
            client = LocationServices.getFusedLocationProviderClient(this)
            buildNotification()
            requestLocationUpdates()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun buildNotification() {
        val stop = "stop"
        registerReceiver(stopReceiver, IntentFilter(stop))
        val broadcastIntent = PendingIntent.getBroadcast(
            this, 0, Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId =
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                createNotificationChannel(channelID, channelName)
            } else {
                ""
            }

        // Create the persistent notification
        val builder = NotificationCompat.Builder(this, channelId)
            .setContentTitle(getString(com.example.dogwalker.R.string.app_name))
            .setContentText(getString(com.example.dogwalker.R.string.notification_text))
            .setOngoing(true)
            .setContentIntent(broadcastIntent)
            .setSmallIcon(R.drawable.ic_tracker)
        startForeground(TRACKER_NOTIFICATION_ID, builder.build())
    }

    override fun onDestroy() {
        //Remove realtime database updater
        client.removeLocationUpdates(locationCallBack)

        //Unregister customer broadcast receiver
        unregisterReceiver(stopReceiver)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelID: String, channelName: String) :String {
        val chan = NotificationChannel(channelID,
            channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelID
    }

    private fun requestLocationUpdates() {
        val request = LocationRequest()
        request.interval = 2000
        request.fastestInterval = 1000
        request.priority = LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY
//        request.smallestDisplacement = 50.toFloat()
        val path = "walker/$phone/position"
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (permission == PackageManager.PERMISSION_GRANTED) {

            // Request location updates and when an update is
            // received, store the location in Firebase
            locationCallBack = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult?) {
                    val ref = FirebaseDatabase.getInstance().getReference(path)
                    val location = locationResult!!.lastLocation
                    if (location != null) {
                        Log.d(TAG, "location update $location")
                        val locations = Location(location.latitude, location.longitude)
                        ref.setValue(locations)
                    }
                }
            }

            client.requestLocationUpdates(request, locationCallBack, null)
        }
    }
}