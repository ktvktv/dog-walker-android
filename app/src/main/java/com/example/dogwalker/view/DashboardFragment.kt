package com.example.dogwalker.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.dogwalker.R
import com.example.dogwalker.WalkerOrderActivity
import com.example.dogwalker.databinding.FragmentDashboardBinding

class DashboardFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDashboardBinding.inflate(inflater)

        binding.findWalkerButton.setOnClickListener {
            startActivity(Intent(context, WalkerOrderActivity::class.java))
        }

        return binding.root
    }
}