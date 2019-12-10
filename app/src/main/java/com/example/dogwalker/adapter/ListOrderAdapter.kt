package com.example.dogwalker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogwalker.R
import com.example.dogwalker.data.Walker
import kotlinx.android.synthetic.main.order_item.view.*
import kotlinx.android.synthetic.main.post_item.view.*

class ListOrderAdapter(val listOrder: List<Walker>, val context: Context,
                       val listOrderOnClickListener: ListOrderOnClickListener) : RecyclerView.Adapter<ListOrderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false)

        return ViewHolder(view, listOrderOnClickListener)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.rating_bar_order.rating = listOrder[position].rating
        holder.itemView.description_order_text.text = listOrder[position].description
        holder.itemView.name_order_text.text = listOrder[position].name

        val imgUri = listOrder[position].imageUrl.toUri().buildUpon().scheme("https").build()
        val imageView = holder.itemView.profile_order_image

        Glide.with(imageView.context)
            .load(imgUri)
            .into(imageView)
    }

    class ViewHolder(val view: View, val listOrderOnClickListener: ListOrderOnClickListener) : RecyclerView.ViewHolder(view) {

        init {
            view.setOnClickListener {
                listOrderOnClickListener.pickOrder(this.layoutPosition)
            }
        }

//        override fun onClick(v: View?) {
//            listOrderOnClickListener.pickOrder(this.layoutPosition)
//        }

    }

    interface ListOrderOnClickListener {
        fun pickOrder(position: Int)
    }
}