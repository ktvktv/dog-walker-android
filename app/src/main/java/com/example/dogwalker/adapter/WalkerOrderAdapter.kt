package com.example.dogwalker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dogwalker.R
import kotlinx.android.synthetic.main.dog_item.view.*

class WalkerOrderAdapter(val listDog: List<Int>, val context: Context) : RecyclerView.Adapter<WalkerOrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.dog_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDog.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.dog_image_order_item.setImageResource(listDog[position])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}