package com.example.dogwalker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
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

        registerWalkerViewModel.registerWalkerResponse.observe(this, Observer {
            if(it != null) {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                if(it.message == LOGIN_SUCCESSFUL) {
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
}