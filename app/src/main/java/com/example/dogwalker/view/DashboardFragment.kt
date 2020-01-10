package com.example.dogwalker.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dogwalker.MainActivity
import com.example.dogwalker.R
import com.example.dogwalker.WalkerOrderActivity
import com.example.dogwalker.databinding.FragmentDashboardBinding

class DashboardFragment: Fragment() {
    private val TAG = DashboardFragment::class.java.simpleName
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater)

        binding.dashboardView.setOnClickListener {
            Log.d(TAG, "Nothing happen")
        }

        binding.dogDashboardImage.setOnClickListener {
            val intent = Intent(context, WalkerOrderActivity::class.java)
            startActivity(intent)
        }

        binding.logoutButton.setOnClickListener{
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            context?.deleteSharedPreferences(getString(R.string.preferences_file_key))

            startActivity(intent)
        }

        return binding.root
    }
}