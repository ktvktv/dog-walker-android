package com.example.dogwalker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.dogwalker.data.Notification
import com.example.dogwalker.data.TransactionStatus
import com.example.dogwalker.network.DogWalkerServiceApi
import com.example.dogwalker.service.DogWalkerService
import com.example.dogwalker.view.OngoingOrderFragment
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private val LOCATION_PERMISSIONS_REQUEST = 1

    private val TAG = MapsActivity::class.java.getSimpleName()
    private val mMarkers = HashMap<String, Marker>()
    private var mMap: GoogleMap? = null
    private val polylineOptions = PolylineOptions()
    private lateinit var polyLines: Polyline
    private lateinit var userType: String
    private lateinit var phone: String
    private var firstTime: Boolean = true

    companion object Constants {
        val PHONE_EXTRA = "phone"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val buttonView = findViewById<Button>(R.id.endWalkButton)
        val id = intent.extras.getInt("id")

        val session = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        buttonView.setOnClickListener{
            CoroutineScope(Job() + Dispatchers.Main).launch {
                DogWalkerServiceApi.DogWalkerService.changeTransactionStatus(
                    session, TransactionStatus(
                        id, "DONE"
                    )
                )!!.await()

                DogWalkerServiceApi.DogWalkerService.sendNotification(session, Notification(
                    "Order selesai",
                    "Terima kasih telah menggunakan jasa kami",
                    "Walker",
                    "Rating",
                    id
                ))
            }

            FirebaseDatabase.getInstance().getReference("walker/$phone/done").setValue("Yes")

            Toast.makeText(this, "Transaksi selesai!", Toast.LENGTH_SHORT).show()
            stopService(Intent(this, DogWalkerService::class.java))
            setResult(RESULT_OK)
            finish()
        }

        firstTime = true

        userType = intent.extras.get(OngoingOrderFragment.USER_TYPE) as String
        phone = intent.extras.get(OngoingOrderFragment.PHONE_EXTRA) as String

        // Check GPS is enabled
        if(userType.toLowerCase() == "walker") {
            val lm = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(this, "Tolong nyalakan GPS", Toast.LENGTH_SHORT).show()
                finish()
            }

//            Check location Manifest.permission is granted -if it is, start
//            the service, otherwise request the permission
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
                    LOCATION_PERMISSIONS_REQUEST
                )
            }
        } else {
            buttonView.visibility = View.GONE
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    private fun startTrackerService() {
        val intent = Intent(this, DogWalkerService::class.java)
        Log.d(TAG, phone)
        intent.putExtra(PHONE_EXTRA, phone)

        startService(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == LOCATION_PERMISSIONS_REQUEST && grantResults.size == 1
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
        mMap!!.setMaxZoomPreference(17f)

        subscribeToUpdates()
    }

    private fun subscribeToUpdates() {
        Log.d(TAG, "Subscribe to walker's location, phone: $phone")
        val ref = FirebaseDatabase.getInstance().getReference("walker/$phone/position")
//        val locationListener = object : ChildEventListener {
//            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                Log.d(TAG, "child name: $previousChildName")
//                if(previousChildName != "done") {
//                    setMarker(dataSnapshot)
//                }
//            }
//
//            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
//                Log.d(TAG, "child name: $previousChildName")
//                if(previousChildName != "done") {
//                    setMarker(dataSnapshot)
//                }
//            }
//
//            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}
//
//            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d(TAG, "Failed to read value.", error.toException())
//            }
//        }
//
//        ref.addChildEventListener(locationListener)
        val locationListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "Cancel location")
            }

            override fun onDataChange(p0: DataSnapshot) {
                setMarker(p0)
            }

        }

        ref.addValueEventListener(locationListener)

        Log.d(TAG, "Subscribe to walker's end walk state")
        val doneRef = FirebaseDatabase.getInstance().getReference("walker/$phone/done")
        val doneListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e(TAG, "Cancel Done")
            }

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()) {
                    val doneValue = p0.value as String

                    Log.d(TAG, "Done's walker value: $doneValue")
                    if (doneValue == "Yes") {
                        ref.removeEventListener(locationListener)
                        Toast.makeText(
                            this@MapsActivity,
                            "Sesi jalan selesai!",
                            Toast.LENGTH_SHORT
                        ).show()
                        setResult(RESULT_OK)
                        finish()
                    }
                }
            }
//            override fun onCancelled(error: DatabaseError) {
//                Log.d(TAG, "Failed to read value.", error.toException())
//            }
//
//            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
//                //
//            }
//
//            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
//
//            }
//
//            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
//                val doneValue = p0.value as String
//
//                Log.d(TAG, doneValue)
//
//                if(doneValue.equals("Yes")) {
//                    ref.removeEventListener(locationListener)
////                    polyLines.remove()
//
//                    //Call network operation to notify the order's done.
//
//                    Log.d(TAG, "doneListener called")
//
//                    Toast.makeText(this@MapsActivity, "Walking session is done", Toast.LENGTH_SHORT).show()
//
//                    finish()
//                }
//            }
//
//            override fun onChildRemoved(p0: DataSnapshot) {
//                //
//            }
        }

        doneRef.addValueEventListener(doneListener)
    }

    private fun setMarker(dataSnapshot: DataSnapshot) {
        if(firstTime) {
            firstTime = false
            return
        }
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        val key = dataSnapshot.key
        Log.d(TAG, "Data: $dataSnapshot")
        Log.d(TAG, "Key: $key")
        val value = dataSnapshot.value as HashMap<String, Any>
        val lat = java.lang.Double.parseDouble(value["latitude"].toString())
        val lng = java.lang.Double.parseDouble(value["longitude"].toString())
        val location = LatLng(lat, lng)

        polylineOptions.add(location)
        if (!mMarkers.containsKey(key)) {
            mMarkers[key!!] = mMap!!.addMarker(MarkerOptions().title(key).position(location))
        } else {
            mMarkers[key]!!.position = location
        }

        val builder = LatLngBounds.Builder()
        for (marker in mMarkers.values) {
            builder.include(marker.position)
        }

        mMap!!.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 300))

        polyLines = mMap!!.addPolyline(polylineOptions)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item?.itemId) {
            android.R.id.home -> {
                setResult(RESULT_OK)
                finish()
            }
        }
        return true
    }

    override fun onBackPressed() {
        setResult(RESULT_OK)
        super.onBackPressed()
    }
}