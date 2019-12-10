package com.example.dogwalker.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalker.MapsActivity
import com.example.dogwalker.adapter.OngoingOrderAdapter
import com.example.dogwalker.data.Order
import com.example.dogwalker.databinding.FragmentOngoingOrderBinding

class OngoingOrderFragment(val userType: String) : Fragment(), OngoingOrderAdapter.OngoingClickListener {

    companion object constants {
        val PHONE_EXTRA = "phone"
        val USER_TYPE = "user-type"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOngoingOrderBinding.inflate(inflater)

        val listOrder = listOf(
            Order(
                name = "Wijoyo",
                phone = "081293312313",
                status = "On Going",
                date = "18:00 09/09/1998 - 23:00 09/09/1998",
                address = "Jl. Barleria VI B1/H5"
            ),
            Order(
                name = "Wijoyo",
                phone = "081293312313",
                status = "On Going",
                date = "18:00 09/09/1998 - 23:00 09/09/1998",
                address = "Jl. Barleria VI B1/H5"
            )
        )

        binding.ongoingOrderRecycler.adapter = OngoingOrderAdapter(userType, listOrder, this)
        binding.ongoingOrderRecycler.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        return binding.root
    }

    override fun onClick(phone: String, userType: String) {
        val intent = Intent(context, MapsActivity::class.java)
        intent.putExtra(PHONE_EXTRA, phone)
        intent.putExtra(USER_TYPE, userType)

        startActivity(intent)
    }
}