package com.example.dogwalker.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogwalker.DashboardActivity
import com.example.dogwalker.R
import com.example.dogwalker.adapter.ListOrderAdapter
import com.example.dogwalker.data.Walker
import kotlinx.android.synthetic.main.fragment_list_order.view.*
import java.lang.Exception

class ListOrderFragment : Fragment(), ListOrderAdapter.ListOrderOnClickListener,
    ListOrderAdapter.CheckWalkerListener {

    val TAG = ListOrderFragment::class.java.simpleName
    val args: ListOrderFragmentArgs by navArgs()

    //TODO:Create View Model

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "${args.dogId}/${args.date}/${args.hours}/${args.time}")
        val view = inflater.inflate(R.layout.fragment_list_order, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list_order_recycler)

        val dummyData = Walker(
            1,
            "",
            "",
            "",
            "Kevin",
            "",
            "Male",
            "",
            true,
            "",
            "",
            "",
            "https://cbbinus.files.wordpress.com/2017/05/p_20170504_092501_bf1.jpg?w=700",
            true,
            true,
            "Hello guys aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
            1,
            1,
            1,
            1,
            1,
            1
        )

        recyclerView.adapter = ListOrderAdapter(listOf(dummyData), context!!, this, this)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun pickOrder(walkerId: Int, price: Int) {
        val action = ListOrderFragmentDirections.actionListOrderFragmentToOrderDetailFragment(
            walkerId, args.dogId, args.date, args.time, args.hours, price*args.hours
        )

        findNavController().navigate(action)
    }

    override fun checkWalker(walkerId: Int) {
        Log.d(TAG, "WalkerID ListOrder: $walkerId")

        val action = ListOrderFragmentDirections.actionListOrderFragmentToWalkerInfoFragment(walkerId)

        findNavController().navigate(action)
    }
}