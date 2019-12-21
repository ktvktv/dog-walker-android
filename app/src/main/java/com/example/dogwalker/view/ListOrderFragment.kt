package com.example.dogwalker.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dogwalker.DashboardActivity
import com.example.dogwalker.R
import com.example.dogwalker.adapter.ListOrderAdapter
import com.example.dogwalker.data.Walker
import kotlinx.android.synthetic.main.fragment_list_order.view.*

class ListOrderFragment : Fragment(), ListOrderAdapter.ListOrderOnClickListener {
    val TAG = ListOrderFragment::class.java.simpleName
    val args: ListOrderFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "${args.breedId}/${args.date}/${args.hours}/${args.time}")
        val view = inflater.inflate(R.layout.fragment_list_order, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.list_order_recycler)

        recyclerView.adapter = ListOrderAdapter(listOf(), context!!, this)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun pickOrder(position: Int) {
        //Send message
        Log.d(TAG, "Message has been fired, position: $position")

        activity?.finish()
    }
}