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
import kotlinx.android.synthetic.main.info_item.view.*
import kotlinx.android.synthetic.main.post_item.view.*

class InfoAdapter(var listData: List<Dog>, val context: Context) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {
    private val TAG = InfoAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.info_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(listData[position].photo != null) {
            val imgUri = listData[position].photo!!.toUri().buildUpon().scheme("https").build()
            val imageView = holder.itemView.dog_image_info

//            Log.d(TAG, "Dog URL: ${listData[position].photo}")

            Glide.with(imageView.context)
                .load(imgUri)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(imageView)
        }

        holder.itemView.dog_name_info.text = listData[position].name
        holder.itemView.dog_age_info.text = "${listData[position].age} bulan"
        holder.itemView.weight_text.text = "${listData[position].weight} Kilogram"
        holder.itemView.breed_info.text = listData[position].breedName

        if(listData[position].gender.toLowerCase() == "male") {
            holder.itemView.gender_info.setImageResource(R.drawable.male_icon)
        } else {
            holder.itemView.gender_info.setImageResource(R.drawable.female_icon)
        }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}