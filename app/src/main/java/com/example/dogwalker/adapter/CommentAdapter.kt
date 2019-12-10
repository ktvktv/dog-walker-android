package com.example.dogwalker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dogwalker.R
import com.example.dogwalker.data.Comment
import kotlinx.android.synthetic.main.activity_single_post.view.*
import kotlinx.android.synthetic.main.comment_item.view.*
import kotlinx.android.synthetic.main.info_item.view.*

class CommentAdapter(val commentData: List<Comment>) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.comment_item, parent, false)
        
        view.comment_scroll_view.setOnTouchListener { v, event ->
            v.parent.requestDisallowInterceptTouchEvent(true)

            v.onTouchEvent(event)

            true
        }

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.name_comment.text = commentData[position].name
        holder.itemView.content_comment.text = commentData[position].content
        holder.itemView.date_comment.text = commentData[position].date

        val imgUri = commentData[position].image.toUri().buildUpon().scheme("https").build()
        val imageView = holder.itemView.image_comment

        Glide.with(imageView.context)
            .load(imgUri)
            .into(imageView)
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}