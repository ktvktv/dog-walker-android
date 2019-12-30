package com.example.dogwalker.view

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalker.MapsActivity
import com.example.dogwalker.R
import com.example.dogwalker.adapter.OngoingOrderAdapter
import com.example.dogwalker.data.NotifyData
import com.example.dogwalker.data.Order
import com.example.dogwalker.data.TransactionStatus
import com.example.dogwalker.databinding.FragmentOngoingOrderBinding
import com.example.dogwalker.network.DogWalkerServiceApi
import com.example.dogwalker.viewmodel.OngoingOrderViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OngoingOrderFragment : Fragment(), OngoingOrderAdapter.OngoingClickListener, OngoingOrderAdapter.PendingClickListener{

    private val TAG = OngoingOrderFragment::class.java.simpleName
    private val ongoingOrderViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(OngoingOrderViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private lateinit var ongoingOrderAdapter: OngoingOrderAdapter

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

        val sharedPreferences = activity!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        val session = sharedPreferences.getString(getString(R.string.session_cache), "")
        val type = sharedPreferences.getString(getString(R.string.type_cache), "")

        if(type.toLowerCase() == "customer") {
            coroutineScope.launch {
                ongoingOrderViewModel.getListOrder(session)
            }
        } else {
            coroutineScope.launch {
                ongoingOrderViewModel.getWalkerListOrder(session)
            }
        }

        ongoingOrderViewModel.orderList.observe(this, Observer{
            if(it != null) {
                Log.d(TAG, "$it")
                ongoingOrderAdapter.listOrder = it
                ongoingOrderAdapter.notifyDataSetChanged()
            }
        })

        val intent = activity!!.intent
        var isFromNotify = intent.getBooleanExtra("isFromNotify", false)

        var transactionID = -1
        val transactionIDString = intent.getStringExtra("id")

        if(transactionIDString != null) {
            transactionID = transactionIDString.toInt()
        }

        if(type != "" && type.toLowerCase() == "walker" && isFromNotify && transactionID > 0) {
            pendingClick(
                NotifyData(
                    transactionID,
                    intent.getStringExtra("photo"),
                    intent.getStringExtra("description"),
                    intent.getStringExtra("date"),
                    intent.getStringExtra("body"),
                    intent.getStringExtra("title")
                )
            )

            intent.putExtra("isFromNotify", false)
        }

        ongoingOrderAdapter = OngoingOrderAdapter(type, listOf(), this, this, context!!)
        binding.ongoingOrderRecycler.adapter = ongoingOrderAdapter
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
        val newFragment = OrderDecisionFragment(pendingData, ongoingOrderViewModel)

        newFragment.show(activity!!.supportFragmentManager, "Dialog")
    }

    override fun onResume() {
        val sharedPreferences = activity!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        val session = sharedPreferences.getString(getString(R.string.session_cache), "")
        val type = sharedPreferences.getString(getString(R.string.type_cache), "")

        if(type.toLowerCase() == "customer") {
            coroutineScope.launch {
                ongoingOrderViewModel.getListOrder(session)
            }
        } else {
            coroutineScope.launch {
                ongoingOrderViewModel.getWalkerListOrder(session)
            }
        }
        super.onResume()
    }
}