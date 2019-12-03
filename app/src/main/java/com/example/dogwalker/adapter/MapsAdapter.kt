package com.example.dogwalker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dogwalker.R
import com.example.dogwalker.data.OrderWalker

class MapsAdapter(val listOrder: List<OrderWalker>) : RecyclerView.Adapter<MapsAdapter.MapsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.maps_order_item, parent, false)

        return MapsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: MapsViewHolder, position: Int) {

    }

    class MapsViewHolder(val itemView: View): RecyclerView.ViewHolder(itemView) {

    }
}