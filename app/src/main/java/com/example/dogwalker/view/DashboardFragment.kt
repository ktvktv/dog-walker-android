package com.example.dogwalker.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dogwalker.WalkerOrderActivity
import com.example.dogwalker.databinding.FragmentDashboardBinding

class DashboardFragment: Fragment() {

    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater)

        binding.dogDashboardImage.setOnClickListener {
            val intent = Intent(context, WalkerOrderActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}