package com.example.dogwalker.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalker.MapsActivity
import com.example.dogwalker.R
import com.example.dogwalker.adapter.OngoingOrderAdapter
import com.example.dogwalker.data.NotifyData
import com.example.dogwalker.data.Order
import com.example.dogwalker.databinding.FragmentOngoingOrderBinding
import com.example.dogwalker.viewmodel.OngoingOrderViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory

class OngoingOrderFragment : Fragment(), OngoingOrderAdapter.OngoingClickListener, OngoingOrderAdapter.PendingClickListener{

    private val ongoingOrderViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(OngoingOrderViewModel::class.java)
    }

    companion object {
        val PHONE_EXTRA = "phone"
        val USER_TYPE = "user-type"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOngoingOrderBinding.inflate(inflater)

        val orderList = ongoingOrderViewModel.getOrderList()

        val phoneNumber = activity!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.phone_number_cache), "")

        binding.ongoingOrderRecycler.adapter = OngoingOrderAdapter("Customer", orderList, this, this, phoneNumber)
        binding.ongoingOrderRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

    override fun onClick(phone: String, userType: String) {
        val intent = Intent(context, MapsActivity::class.java)
        intent.putExtra(PHONE_EXTRA, phone)
        intent.putExtra(USER_TYPE, userType)

        startActivity(intent)
    }

    override fun pendingClick(pendingData: NotifyData) {
        val newFragment = OrderDecisionFragment(pendingData)

//        newFragment.dialog!!.window.setLayout(300, 350)

        newFragment.show(activity!!.supportFragmentManager, "Dialog")
    }
}