package com.example.dogwalker.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogwalker.R
import com.example.dogwalker.SinglePost
import com.example.dogwalker.data.Post
import kotlinx.android.synthetic.main.post_item.view.*

class PostViewAdapter(val list: List<Post>, val listener: PostViewAdapterClickListener)
    : RecyclerView.Adapter<PostViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)

        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.name_view.text = list[position].name
        holder.itemView.title_view.text = list[position].title
        holder.itemView.content_view.text = list[position].content

        val imgUri = list[position].image.toUri().buildUpon().scheme("https").build()
        val imageView = holder.itemView.image_post

        Glide.with(imageView.context)
            .load(imgUri)
            .into(imageView)
    }

    class ViewHolder(itemView: View,
                     val listener: PostViewAdapterClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.postViewClickListener(this.layoutPosition)
        }
    }

    interface PostViewAdapterClickListener {
        public fun postViewClickListener(position: Int)
    }
}