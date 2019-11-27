package com.example.dogwalker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dogwalker.service.DogWalkerService
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.FirebaseDatabase
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val PERMISSIONS_REQUEST = 1
    private val TAG = MapsActivity::class.java.getSimpleName()
    private val mMarkers = HashMap<String, Marker>()
    private var mMap: GoogleMap? = null
    private val polylineOptions = PolylineOptions()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        // Check GPS is enabled
        val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        val permission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            startTrackerService()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST
            )
        }

        val endButton = findViewById<Button>(R.id.end_button)

        endButton.setOnClickListener {
            stopService(Intent(this, DogWalkerService::class.java))
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun startTrackerService() {
        startService(Intent(this, DogWalkerService::class.java))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.size == 1
            && grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            // Start the service when the permission is granted
            startTrackerService()
        } else {
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        // Authenticate with Firebase when the Google map is loaded
        mMap = googleMap
        mMap!!.setMaxZoomPreference(18f)
        subscribeToUpdates()
    }

    private fun subscribeToUpdates() {
        val ref = FirebaseDatabase.getInstance().getReference(getString(R.string.firebase_path))
        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                setMarker(dataSnapshot)
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                setMarker(dataSnapshot)
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onCancelled(error: DatabaseError) {
                Log.d(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun setMarker(dataSnapshot: DataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        val key = dataSnapshot.key
        val value = dataSnapshot.value as HashMap<String, Any>
        val lat = java.lang.Double.parseDouble(value["latitude"].toString())
        val lng = java.lang.Double.parseDouble(value["longitude"].toString())
        val location = LatLng(lat, lng)
        polylineOptions.add(location)
        if (!mMarkers.containsKey(key)) {
            mMarkers[key!!] = mMap!!.addMarker(MarkerOptions().title(key).position(location))
        } else {
            mMarkers[key]!!.setPosition(location)
        }
        val builder = LatLngBounds.Builder()
        for (marker in mMarkers.values) {
            builder.include(marker.position)
        }
        mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300))
        mMap!!.addPolyline(polylineOptions)
    }
}