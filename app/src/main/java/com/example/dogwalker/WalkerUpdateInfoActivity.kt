package com.example.dogwalker

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dogwalker.data.WalkerUpdateRequest
import com.example.dogwalker.databinding.ActivityWalkerRegisterBinding
import com.example.dogwalker.viewmodel.ViewModelFactory
import com.example.dogwalker.viewmodel.WalkerUpdateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception

class WalkerUpdateInfoActivity : AppCompatActivity() {
    private val TAG = WalkerUpdateInfoActivity::class.java.simpleName
    private val walkerUpdateInfoViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(WalkerUpdateViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val session = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        val walkerUpdateBinding = ActivityWalkerRegisterBinding.inflate(LayoutInflater.from(this))

        walkerUpdateBinding.walkerRegisterTitle.text = "PERBARUI WALKER"
        walkerUpdateBinding.registerButton.text = "Ubah"

        coroutineScope.launch {
            walkerUpdateInfoViewModel.getWalker(session)
        }

        walkerUpdateInfoViewModel.walkerDataResponse.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL && it.body != null) {
                val data = it.body

                Log.d(TAG, "Walker Data: $data")

                walkerUpdateBinding.descriptionText.setText(data.description)
                walkerUpdateBinding.maxDistanceText.setText(data.travelDistance.toString())
                walkerUpdateBinding.maxDurationText.setText(data.walkDuration.toString())
                walkerUpdateBinding.pricingText.setText(data.pricing.toString())
                walkerUpdateBinding.dogWeightText.setText(data.maxDogSize.toString())
            }
        })

        walkerUpdateInfoViewModel.walkerUpdateResponse.observe(this, Observer {
            if(it != null) {
                if(it.message == SUCCESSFUL) {
                    Toast.makeText(this, "Berhasil memperbarui walker", Toast.LENGTH_SHORT).show()

                    finish()
                } else {
                    Toast.makeText(this, "Gagal memperbarui walker", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Terjadi kesalahan, silahkan coba lagi", Toast.LENGTH_SHORT).show()
            }
        })

        val alertDialog = AlertDialog.Builder(ContextThemeWrapper(this, android.R.style.Theme_Material_Light_Dialog_Alert))
            .setTitle("Informasi")
            .setPositiveButton("OK") { dialogInterface, i ->
                dialogInterface.dismiss()
            }.create()

        walkerUpdateBinding.infoButtonMaxDog.setOnTouchListener { view, motionEvent ->
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

        walkerUpdateBinding.infoButtonMaxDist.setOnTouchListener { view, motionEvent ->
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

        walkerUpdateBinding.infoButtonMaxDuration.setOnTouchListener { view, motionEvent ->
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

        walkerUpdateBinding.infoButtonPricing.setOnTouchListener { view, motionEvent ->
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

        walkerUpdateBinding.registerButton.setOnClickListener {
            var maxDogSize: Int
            try {
                maxDogSize = Integer.parseInt(walkerUpdateBinding.dogWeightText.text.toString())
            } catch(e: Exception) {
                Log.e(TAG, "Parse error, err: ${e.message}")
                e.printStackTrace()
                Toast.makeText(this, "Maksimal berat anjing harus dalam bentuk angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var pricing: Int
            try {
                pricing = Integer.parseInt(walkerUpdateBinding.pricingText.text.toString())
            } catch(e: Exception) {
                Log.e(TAG, "Parse error, err: ${e.message}")
                e.printStackTrace()
                Toast.makeText(this, "Harga harus dalam bentuk angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var travelDistance: Int
            try {
                travelDistance = Integer.parseInt(walkerUpdateBinding.maxDistanceText.text.toString())
            } catch(e: Exception) {
                Log.e(TAG, "Parse error, err: ${e.message}")
                e.printStackTrace()
                Toast.makeText(this, "Maksimal jarak pengambilan anjing harus dalam bentuk angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            var walkDuration: Int
            try {
                walkDuration = Integer.parseInt(walkerUpdateBinding.maxDurationText.text.toString())
            } catch(e: Exception) {
                Log.e(TAG, "Parse error, err: ${e.message}")
                e.printStackTrace()
                Toast.makeText(this, "Durasi dalam menjalankan anjing harus dalam bentuk angka", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val walkerRequest = WalkerUpdateRequest(
                description = walkerUpdateBinding.descriptionText.text.toString(),
                maxDogSize = maxDogSize,
                pricing = pricing,
                travelDistance = travelDistance,
                walkDuration = walkDuration
            )

            coroutineScope.launch {
                walkerUpdateInfoViewModel.updateWalker(session, walkerRequest)
            }
        }

        setContentView(walkerUpdateBinding.root)
    }
}