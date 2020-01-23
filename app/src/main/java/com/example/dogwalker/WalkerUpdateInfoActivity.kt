package com.example.dogwalker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
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

        walkerUpdateBinding.walkerRegisterTitle.text = "Perbarui walker"
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
                walkerUpdateBinding.weightText.setText(data.maxDogSize.toString())
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

        walkerUpdateBinding.registerButton.setOnClickListener {
            var maxDogSize: Int
            try {
                maxDogSize = Integer.parseInt(walkerUpdateBinding.weightText.text.toString())
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