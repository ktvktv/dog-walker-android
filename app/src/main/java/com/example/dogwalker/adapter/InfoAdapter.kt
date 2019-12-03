package com.example.dogwalker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dogwalker.R
import com.example.dogwalker.data.Dog
import kotlinx.android.synthetic.main.info_item.view.*

class InfoAdapter(val listData: List<Dog>, val context: Context) : RecyclerView.Adapter<InfoAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.info_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.dog_name_info.text = listData[position].name
        holder.itemView.dog_age_info.text = listData[position].age.toString()
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}