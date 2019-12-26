package com.example.dogwalker.adapter

import android.content.Context
import android.util.Log
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

class ListOrderAdapter(var listOrder: List<Walker>, val context: Context,
                       val listOrderOnClickListener: ListOrderOnClickListener,
                       val checkWalkerListener: CheckWalkerListener) : RecyclerView.Adapter<ListOrderAdapter.ViewHolder>() {

    private val TAG = ListOrderAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.order_item, parent, false)

        view.order_scroll.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)

            v.onTouchEvent(event)

            true
        }

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rating = if(listOrder[position].raters != 0) {
            listOrder[position].rating/listOrder[position].raters
        } else 0

        holder.itemView.rating_bar_order.rating = rating.toFloat()
        holder.itemView.description_order_text.text = listOrder[position].description
        holder.itemView.name_order_text.text = listOrder[position].name

        holder.itemView.next_button.setOnClickListener {
            listOrderOnClickListener.pickOrder(listOrder[position].id, listOrder[position].pricing)
        }

        if(listOrder[position].photo != null && listOrder[position].photo != "") {
            val imgUri = listOrder[position].photo!!.toUri().buildUpon().scheme("https").build()
            val imageView = holder.itemView.profile_order_image

            Glide.with(imageView.context)
                .load(imgUri)
                .into(imageView)
        }

        holder.itemView.profile_order_image.setOnClickListener {
            checkWalkerListener.checkWalker(listOrder[position].id)
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface ListOrderOnClickListener {
        fun pickOrder(position: Int, price: Int)
    }

    interface CheckWalkerListener {
        fun checkWalker(walkerId: Int)
    }
}