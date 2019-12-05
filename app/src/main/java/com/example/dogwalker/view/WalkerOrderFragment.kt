package com.example.dogwalker.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalker.R
import com.example.dogwalker.adapter.WalkerOrderAdapter
import com.example.dogwalker.databinding.FragmentWalkerOrderBinding
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class WalkerOrderFragment : Fragment() {

    private val TAG = WalkerOrderFragment::class.java.simpleName

    private val DatePickerTAG = "DatePicker"
    private val TimePickerTAG = "TimePicker"

    private lateinit var binding: FragmentWalkerOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "Created View")

        binding = FragmentWalkerOrderBinding.inflate(inflater)
        
        val currentDate = Calendar.getInstance().time
        binding.dogCalendarOrder.text = SimpleDateFormat("dd/MM/yyyy").format(currentDate)
        binding.timeOrderWalker.text = SimpleDateFormat("kk:mm:ss").format(currentDate)

        binding.dogCalendarOrder.setOnClickListener {
            Log.d(TAG, "Calendar clicked")

            val newFragment = DateFragment(it as TextView)
            newFragment.show(fragmentManager!!, DatePickerTAG)
        }

        binding.timeOrderWalker.setOnClickListener {
            Log.d(TAG, "Time clicked")

            val newFragment = TimeFragment(it as TextView)
            newFragment.show(fragmentManager!!, TimePickerTAG)
        }

        binding.walkerOrderButton.setOnClickListener {
            try {
                val hours = Integer.parseInt(binding.hoursText.text.toString())

                val linearLayoutManager = binding.walkerOrderRecyclerView.layoutManager as LinearLayoutManager

                val action = WalkerOrderFragmentDirections.actionWalkerOrderFragmentToListOrderFragment(
                    date = binding.dogCalendarOrder.text.toString(),
                    time = binding.timeOrderWalker.text.toString(),
                    hours = hours,
                    breedId = linearLayoutManager.findFirstVisibleItemPosition()
                )

                it.findNavController().navigate(action)
            } catch(e :Exception) {
                Toast.makeText(context, "Hours must be numeric", Toast.LENGTH_SHORT).show()
            }
        }

        val listData = listOf(R.drawable.man_carry_dog, R.drawable.man_user)

        binding.walkerOrderRecyclerView.adapter = WalkerOrderAdapter(listData, context!!)
        binding.walkerOrderRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        return binding.root
    }
}