package com.example.dogwalker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dogwalker.databinding.FragmentWalkerOrderBinding

class WalkerOrderFragment : Fragment() {

    private lateinit var binding: FragmentWalkerOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWalkerOrderBinding.inflate(inflater)

        return binding.root
    }
}