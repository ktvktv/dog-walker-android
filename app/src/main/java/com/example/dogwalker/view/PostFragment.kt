package com.example.dogwalker.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalker.R
import com.example.dogwalker.SinglePostActivity
import com.example.dogwalker.adapter.PostViewAdapter
import com.example.dogwalker.data.Comment
import com.example.dogwalker.data.InsertPostRequest
import com.example.dogwalker.data.Post
import com.example.dogwalker.databinding.FragmentPostBinding
import com.example.dogwalker.viewmodel.PostViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class PostFragment : Fragment(), PostViewAdapter.PostViewAdapterClickListener, NewPostFragment.PostAddition {

    private val TAG = PostFragment::class.java.simpleName
    private lateinit var binding: FragmentPostBinding
    private lateinit var postAdapter: PostViewAdapter
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)
    private val postViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(PostViewModel::class.java)
    }
    private var listPost = listOf<Post>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater)

        //Get all the post from server
        val session = context!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")

        coroutineScope.launch {
            postViewModel.GetAllPost(session)
        }

        //Attach adapter to the recycler view
        postAdapter = PostViewAdapter(listPost, this)
        binding.postRecyclerView.adapter = postAdapter
        binding.postRecyclerView.layoutManager = LinearLayoutManager(context)

        postViewModel.postList.observe(this, Observer {
            if(it != null) {
                postAdapter.list = it
                postAdapter.notifyDataSetChanged()

                if (binding.swipeRefreshLayout.isRefreshing) {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
        })

        binding.addPostButton.setOnClickListener {
            val newPost = NewPostFragment(this, false, -1)

            val mActivity = activity
            if(mActivity == null) {
                Toast.makeText(context, "Terjadi kesalahan", Toast.LENGTH_SHORT)
                    .show()
            } else {
                newPost.show(mActivity.supportFragmentManager, "dialog")
            }
        }

        binding.swipeRefreshLayout.setOnRefreshListener {
            val session = context!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
                .getString(getString(R.string.session_cache), "")

            coroutineScope.launch {
                postViewModel.GetAllPost(session)
            }
        }

        return binding.root
    }

    override fun postViewClickListener(position: Int) {
        val intent = Intent(context, SinglePostActivity::class.java)
        intent.putExtra("PostFragment", position)

        startActivity(intent)
    }

    override fun addNewPost(content: String, title: String) {
        val newPost = InsertPostRequest(title, content)

        val session = context!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")
        coroutineScope.launch {
            postViewModel.InsertPost(session, newPost)
            postViewModel.GetAllPost(session)
        }
    }

    override fun updateNewPost(content: String, title: String) {
        //No need to do anything
    }
}