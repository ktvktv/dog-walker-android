package com.example.dogwalker.adapter

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogwalker.R
import com.example.dogwalker.data.Comment
import kotlinx.android.synthetic.main.comment_item.view.*

class CommentViewAdapter(val listData: List<Comment>) : RecyclerView.Adapter<CommentViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.name_comment.text = listData[position].name
        holder.itemView.content_comment.text = listData[position].content
        holder.itemView.date_comment.text = listData[position].date

        val imgUri = listData[position].image.toUri().buildUpon().scheme("https").build()
        val imageView = holder.itemView.image_comment

        Glide.with(imageView.context)
            .load(imgUri)
            .into(imageView)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)
}