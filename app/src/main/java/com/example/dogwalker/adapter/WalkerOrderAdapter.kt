package com.example.dogwalker.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dogwalker.R
import com.example.dogwalker.data.Dog
import kotlinx.android.synthetic.main.dog_item.view.*
import kotlinx.android.synthetic.main.info_item.view.*

class WalkerOrderAdapter(var listDog: List<Dog>) : RecyclerView.Adapter<WalkerOrderAdapter.ViewHolder>() {
    private val TAG = WalkerOrderAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dog_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listDog.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(listDog[position].photo != null && listDog[position].photo != "") {
            Log.d(TAG, listDog[position].photo)
            val imgUri = listDog[position].photo!!.toUri().buildUpon().scheme("https").build()
            val imageView = holder.itemView.dog_image_order_item

            Glide.with(imageView.context)
                .load(imgUri)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(imageView)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}