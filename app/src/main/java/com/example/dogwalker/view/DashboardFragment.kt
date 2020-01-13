package com.example.dogwalker.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dogwalker.MainActivity
import com.example.dogwalker.R
import com.example.dogwalker.WalkerOrderActivity
import com.example.dogwalker.data.RatingRequest
import com.example.dogwalker.databinding.FragmentDashboardBinding
import com.example.dogwalker.viewmodel.DashboardViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory

class DashboardFragment: Fragment() {
    private val TAG = DashboardFragment::class.java.simpleName
    private lateinit var binding: FragmentDashboardBinding
    private val dashboardViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(DashboardViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater)

        //TODO:Get latest transaction API

        dashboardViewModel.transactionResponse.observe(this, Observer {
            if(it != null && !it.body.isRated) {
                val ratingFragment = RatingFragment(it.body, dashboardViewModel)

                ratingFragment.show(activity!!.supportFragmentManager, "")
            }
        })

        binding.dashboardView.setOnClickListener {
            Log.d(TAG, "Nothing happen")
        }

        binding.dogDashboardImage.setOnClickListener {
            val intent = Intent(context, WalkerOrderActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}