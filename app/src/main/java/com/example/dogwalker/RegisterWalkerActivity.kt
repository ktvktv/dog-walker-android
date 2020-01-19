package com.example.dogwalker

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dogwalker.data.RegisterWalkerRequest
import com.example.dogwalker.databinding.ActivityWalkerRegisterBinding
import com.example.dogwalker.viewmodel.RegisterWalkerViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class RegisterWalkerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalkerRegisterBinding
    private val registerWalkerViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(RegisterWalkerViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWalkerRegisterBinding.inflate(LayoutInflater.from(this))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        registerWalkerViewModel.registerWalkerResponse.observe(this, Observer {
            if(it != null) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                if(it.message == SUCCESSFUL) {
                    finish()
                }
            }
        })

        binding.registerButton.setOnClickListener {
            val session = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
                .getString(getString(R.string.session_cache), "")

            val walkerData = getWalkerRequest() ?: return@setOnClickListener

            coroutineScope.launch {
                registerWalkerViewModel.registerWalker(session, walkerData)
            }
        }

        val alertDialog = AlertDialog.Builder(ContextThemeWrapper(this, android.R.style.Theme_Material_Light_Dialog_Alert))
            .setTitle("Informasi")
            .setPositiveButton("OK") { dialogInterface, i ->
                dialogInterface.dismiss()
            }.create()

        binding.infoButtonMaxDog.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    alertDialog.setMessage("Jangka berat anjing pada umumnya:\n" +
                        "5.5 kg untuk anjing paling kecil(toy)\n" +
                            "5.5 - 11 kg untuk anjing kecil\n" +
                            "11 - 23 kg untuk anjing sedang\n" +
                            "23 - 45 kg untuk anjing besar\n" +
                            "45 kg keatas untuk anjing sangat besar")
                    alertDialog.show()

                    true
                }
                else -> true
            }
        }

        binding.infoButtonMaxDist.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    alertDialog.setMessage("Field ini menentukan maksimal jarak pengambilan anjing yang anda ingini\n\n" +
                            "Pesanan-pesanan yang akan anda terima pasti kurang dari/sama dengan jarak yang anda isi pada field ini")
                    alertDialog.show()

                    true
                }
                else -> true
            }
        }

        binding.infoButtonMaxDuration.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    alertDialog.setMessage("Field ini menentukan maksimal durasi untuk menjalankan anjing yang anda ingini\n\n" +
                            "Pesanan-pesanan yang akan anda terima pasti kurang dari/sama dengan durasi yang anda isi pada field ini")
                    alertDialog.show()

                    true
                }
                else -> true
            }
        }

        binding.infoButtonPricing.setOnTouchListener { view, motionEvent ->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    alertDialog.setMessage("Field ini menentukan harga/jam untuk kompensasi anda dalam menjalankan anjing\n\n" +
                            "Field ini akan ditunjukkan kepada customer dalam pemilihan walker")
                    alertDialog.show()

                    true
                }
                else -> true
            }
        }

        setContentView(binding.root)
    }

    private fun getWalkerRequest(): RegisterWalkerRequest? {
        var description = binding.descriptionText.text.toString()
        description = if(description == "") return null else description

        var maxDogSize: Int
        try {
            maxDogSize = binding.weightText.text.toString().toInt()
        } catch(e: Exception) {
            Toast.makeText(this, "Weight dog must be numeric", Toast.LENGTH_SHORT).show()
            return null
        }

        var maxDistance: Int
        try {
            maxDistance = binding.maxDistanceText.text.toString().toInt()
        } catch(e: Exception) {
            Toast.makeText(this, "Distance must be numeric", Toast.LENGTH_SHORT).show()
            return null
        }

        var pricing: Int
        try {
            pricing = binding.pricingText.text.toString().toInt()
        } catch(e: Exception) {
            Toast.makeText(this, "Price must be numeric", Toast.LENGTH_SHORT).show()
            return null
        }

        var maxDuration: Int
        try {
            maxDuration = binding.maxDurationText.text.toString().toInt()
        } catch(e: Exception) {
            Toast.makeText(this, "Duration must be numeric", Toast.LENGTH_SHORT).show()
            return null
        }

        return RegisterWalkerRequest(
            description, maxDogSize, pricing, maxDistance, maxDuration
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}