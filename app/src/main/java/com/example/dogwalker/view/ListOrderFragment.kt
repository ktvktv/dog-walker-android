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

        val dummyData = listOf(
            Walker(
                imageUrl = "https://pbs.twimg.com/profile_images/378800000110177275/c441ab64d2e233d63eeed78d5b116571_400x400.jpeg",
                name = "Darren Cavell",
                description = "I'm darren cavell.",
                rating = 5f),
            Walker(
                imageUrl = "https://pbs.twimg.com/profile_images/378800000110177275/c441ab64d2e233d63eeed78d5b116571_400x400.jpeg",
                name = "Willy Setiawan",
                description = "Hello, my name is Willy Setiawan. I have a passion to walking a dog, it's really fun and exciting. Nice to meet you! Happy to be here, meeting you guys. Lorem Ipsum wkwkwkkwkwkwkwkwkwkwkwk",
                rating = 2.6f)
        )

        recyclerView.adapter = ListOrderAdapter(dummyData, context!!, this)
        recyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun pickOrder(position: Int) {
        //Send message
        Log.d(TAG, "Message has been fired, position: $position")

        activity?.onBackPressed()
    }
}