package com.example.dogwalker.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs

class ListOrderFragment : Fragment() {

    val TAG = ListOrderFragment::class.java.simpleName
    val args: ListOrderFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "${args.breedId}/${args.date}/${args.hours}/${args.time}")

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}