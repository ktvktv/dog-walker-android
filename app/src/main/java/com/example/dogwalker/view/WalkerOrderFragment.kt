package com.example.dogwalker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogwalker.R
import com.example.dogwalker.adapter.WalkerOrderAdapter
import com.example.dogwalker.databinding.FragmentWalkerOrderBinding
import java.util.*

class WalkerOrderFragment : Fragment() {

    private lateinit var binding: FragmentWalkerOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalkerOrderBinding.inflate(inflater)

        val listData = listOf(R.drawable.man_carry_dog, R.drawable.man_user)

        binding.walkerOrderRecyclerView.adapter = WalkerOrderAdapter(listData, context!!)
        binding.walkerOrderRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }
}