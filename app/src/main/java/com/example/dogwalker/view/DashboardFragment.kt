package com.example.dogwalker.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dogwalker.MainActivity
import com.example.dogwalker.R
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.WalkerOrderActivity
import com.example.dogwalker.data.RatingRequest
import com.example.dogwalker.databinding.FragmentDashboardBinding
import com.example.dogwalker.viewmodel.DashboardViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DashboardFragment: Fragment() {
    private val TAG = DashboardFragment::class.java.simpleName
    private lateinit var binding: FragmentDashboardBinding
    private val dashboardViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(DashboardViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater)

        dashboardViewModel.transactionResponse.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL && it.body != null && !it.body.isRated) {
                Log.d(TAG, "This is latest transaction: ${it.body}")
                val ratingFragment = RatingFragment(it.body, dashboardViewModel)

                ratingFragment.show(activity!!.supportFragmentManager, "")
            }
        })

        dashboardViewModel.ratingResponse.observe(this, Observer {
            var message = "Berhasil menilai walker!"
            if(it != null && it.message != SUCCESSFUL) {
                message = it.message!!
            } else if(it == null) {
                message = "Terjadi kesalahan!"
            }

            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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

    override fun onResume() {
        val session = activity!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        coroutineScope.launch {
            dashboardViewModel.getTransaction(session)
        }

        super.onResume()
    }
}