package com.example.dogwalker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.example.dogwalker.data.InsertPostRequest
import com.example.dogwalker.databinding.ActivitySinglePostBinding
import com.example.dogwalker.view.CommentFragment
import com.example.dogwalker.view.NewPostFragment
import com.example.dogwalker.viewmodel.SinglePostViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SinglePostActivity: AppCompatActivity(), CommentFragment.CommentAddition, NewPostFragment.PostAddition {
    private val TAG = SinglePostActivity::class.java.simpleName
    private lateinit var binding: ActivitySinglePostBinding
    private lateinit var commentAdapter: CommentAdapter
    private val singlePostViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(SinglePostViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private var isMyPost = false
    private var postId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding = ActivitySinglePostBinding.inflate(LayoutInflater.from(this))

        val session = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        postId = intent?.extras?.getInt("PostFragment") ?: -1

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
                isMyPost = it.isMyPost
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

        singlePostViewModel.updatePostResp.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL) {
                Toast.makeText(this, "Success update the post", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fail to update the post", Toast.LENGTH_SHORT).show()
            }
        })

        singlePostViewModel.deletePostResp.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL) {
                Toast.makeText(this, "Success delete the post", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Fail to delete the post", Toast.LENGTH_SHORT).show()
            }
        })

        binding.floatingActionButton.setOnClickListener {
            Log.d(TAG, "FAB Clicked.")
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val session = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        when {
            item?.itemId == android.R.id.home -> finish()
            item?.itemId == R.id.update_menu -> {
                Log.d(TAG, "Update post menu item selected")

                if(isMyPost) {
                    if(postId == -1) {
                        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    val newPost = NewPostFragment(this, true, postId)
                    newPost.show(supportFragmentManager, "dialog")

                } else {
                    Toast.makeText(this, "This is not your post, so update isn't allowed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            item?.itemId == R.id.delete_menu -> {
                Log.d(TAG, "Delete post menu item selected")
                if(isMyPost) {
                    if(postId == -1) {
                        Toast.makeText(this, "Something went wrong, please try again", Toast.LENGTH_SHORT).show()
                        return false
                    }
                    coroutineScope.launch{
                        singlePostViewModel.deletePost(session, postId)
                    }
                } else {
                    Toast.makeText(this, "This is not your post, so delete isn't allowed", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = MenuInflater(this)

        inflater.inflate(R.menu.post_menu, menu)
        return true
    }

    override fun addNewPost(content: String, title: String) {
        //No need to do anything
    }

    override fun updateNewPost(content: String, title: String) {
        val session = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        if(postId == -1) {
            Toast.makeText(this, "Unknown error, please try again", Toast.LENGTH_SHORT).show()
            return
        }

        coroutineScope.launch {
            singlePostViewModel.updatePost(session, InsertPostRequest(title, content), postId)
        }
    }
}