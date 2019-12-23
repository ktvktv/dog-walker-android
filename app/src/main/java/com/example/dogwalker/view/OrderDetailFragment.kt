package com.example.dogwalker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.dogwalker.databinding.FragmentDetailOrderBinding

class OrderDetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailOrderBinding.inflate(inflater)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}