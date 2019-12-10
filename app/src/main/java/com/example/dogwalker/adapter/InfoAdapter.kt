package com.example.dogwalker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogwalker.R
import com.example.dogwalker.data.Dog
import com.example.dogwalker.data.DogRequest
import kotlinx.android.synthetic.main.info_item.view.*
import kotlinx.android.synthetic.main.post_item.view.*

class InfoAdapter(val listData: List<Dog>, val context: Context) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.info_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imgUri = listData[position].photo.toUri().buildUpon().scheme("https").build()
        val imageView = holder.itemView.dog_image_info

        Glide.with(imageView.context)
            .load(imgUri)
            .into(imageView)

        holder.itemView.dog_name_info.text = listData[position].name
        holder.itemView.dog_age_info.text = listData[position].age.toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}