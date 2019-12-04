package com.example.dogwalker.view

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.example.dogwalker.R
import com.example.dogwalker.WalkerOrderActivity
import com.example.dogwalker.databinding.FragmentDashboardBinding
import java.util.*

class DashboardFragment: Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var dogTimers: dogTimer

    private var dogVisible = MutableLiveData<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater)

        binding.dogDashboardImage.setImageResource(R.drawable.walking_dog)

        binding.dogDashboardImage.setOnClickListener {
            startActivity(Intent(context, WalkerOrderActivity::class.java))
        }

        dogTimers = dogTimer(dogVisible, activity!!)

        dogVisible.observe(this, androidx.lifecycle.Observer {
            if(it) {
                binding.dogDashboardImage.visibility = View.INVISIBLE
            } else {
                binding.dogDashboardImage.visibility = View.VISIBLE
            }
        })

        Timer().scheduleAtFixedRate(dogTimers, 0, 2000)

        return binding.root
    }

    override fun onStart() {
//        dogTimers.run()

        super.onStart()
    }

    override fun onStop() {
        dogTimers.cancel()

        super.onStop()
    }

    override fun onDestroy() {
        dogTimers.cancel()

        super.onDestroy()
    }

    class dogTimer(val dogVisibility: MutableLiveData<Boolean>, val activity: Activity) : TimerTask() {
        private var isVisible = true

        override fun run() {
            activity.runOnUiThread {
                if(!isVisible) {
                    dogVisibility.value = true
                    isVisible = true
                } else {
                    dogVisibility.value = false
                    isVisible = false
                }
            }

        }

    }
}