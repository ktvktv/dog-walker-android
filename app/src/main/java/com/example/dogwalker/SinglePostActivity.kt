package com.example.dogwalker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.dogwalker.adapter.CommentAdapter
import com.example.dogwalker.data.Comment
import com.example.dogwalker.data.InsertCommentRequest
import com.example.dogwalker.databinding.ActivitySinglePostBinding
import com.example.dogwalker.view.CommentFragment
import com.example.dogwalker.viewmodel.SinglePostViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SinglePostActivity: AppCompatActivity(), CommentFragment.CommentAddition {

    private lateinit var binding: ActivitySinglePostBinding
    private lateinit var commentAdapter: CommentAdapter
    private val singlePostViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(SinglePostViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySinglePostBinding.inflate(LayoutInflater.from(this))

        val session = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        val postId = intent.extras.getInt("PostFragment")

        coroutineScope.launch {
            singlePostViewModel.getPostDetail(session, postId)
            singlePostViewModel.getCommentDetail(session, postId)
        }

        commentAdapter = CommentAdapter(listOf())
        binding.commentView.adapter = commentAdapter
        binding.commentView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        singlePostViewModel.postDetail.observe(this, Observer{
            if(it != null) {
                if(it.photo != null) {
                    val imgUri = it.photo.toUri().buildUpon().scheme("https").build()
                    val imageView = binding.profilePost

                    Glide.with(imageView.context)
                        .load(imgUri)
                        .into(imageView)
                }

                binding.contentText.text = it.content
                binding.titleText.text = it.title
                binding.namePostView.text = it.name
            }
        })

        singlePostViewModel.commentDetail.observe(this, Observer {
            if(it != null) {
                commentAdapter.commentData = it
                commentAdapter.notifyDataSetChanged()
            }
        })

        singlePostViewModel.message.observe(this, Observer {
            if(it != null) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        binding.floatingActionButton.setOnClickListener {
            Log.d("SinglePostActivity", "FAB Clicked.")

            val fragments = CommentFragment(this, intent.extras.getInt("PostFragment"))

            fragments.show(supportFragmentManager, "dialog")

//            supportFragmentManager.beginTransaction()
//                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
//                .add(CommentFragment(), null)
//                .show(CommentFragment())
//                .commit()
        }

        setContentView(binding.root)
    }

    override fun addNewComment(comment: String, postId: Int) {
        val newComment = InsertCommentRequest(
            postId = postId,
            comment = comment
        )

        val session = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        coroutineScope.launch {
            singlePostViewModel.insertComment(session, newComment)
            singlePostViewModel.getCommentDetail(session, postId)
        }
    }
}