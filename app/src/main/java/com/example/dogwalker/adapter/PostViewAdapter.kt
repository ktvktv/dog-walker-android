package com.example.dogwalker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dogwalker.R
import com.example.dogwalker.data.Post
import kotlinx.android.synthetic.main.post_item.view.*

class PostViewAdapter(var list: List<Post>, val listener: PostViewAdapterClickListener)
    : RecyclerView.Adapter<PostViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.post_item, parent, false)

        view.description_scroll.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)

            v.onTouchEvent(event)

            true
        }

        return ViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.name_view.text = list[position].name
        holder.itemView.title_view.text = list[position].title
        holder.itemView.description_view.text = list[position].content

        if(list[position].photo != null) {
            val imgUri = list[position].photo!!.toUri().buildUpon().scheme("https").build()
            val imageView = holder.itemView.image_post

            Glide.with(imageView.context)
                .load(imgUri)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(imageView)
        }
    }

    inner class ViewHolder(itemView: View,
                     val listener: PostViewAdapterClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            listener.postViewClickListener(list[this.layoutPosition].id)
        }
    }

    interface PostViewAdapterClickListener {
        fun postViewClickListener(position: Int)
    }
}