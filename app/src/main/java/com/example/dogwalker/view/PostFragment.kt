package com.example.dogwalker.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalker.SinglePostActivity
import com.example.dogwalker.adapter.PostViewAdapter
import com.example.dogwalker.data.Comment
import com.example.dogwalker.data.Post
import com.example.dogwalker.databinding.FragmentPostBinding
import com.example.dogwalker.viewmodel.PostViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory

class PostFragment : Fragment(), PostViewAdapter.PostViewAdapterClickListener, NewPostFragment.PostAddition {

    private lateinit var binding: FragmentPostBinding
    private lateinit var postAdapter: PostViewAdapter
    private val postViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(PostViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater)

        //Get all the post from server
        val listPost = postViewModel.getAllPost()

        //Attach adapter to the recycler view
        postAdapter = PostViewAdapter(listPost, this)
        binding.postRecyclerView.adapter = postAdapter
        binding.postRecyclerView.layoutManager = LinearLayoutManager(context)

        binding.addPostButton.setOnClickListener {
            val newPost = NewPostFragment(this)

            val mActivity = activity
            if(mActivity == null) {
                Toast.makeText(context, "Something went wrong when create a post", Toast.LENGTH_SHORT)
                    .show()
            } else {
                newPost.show(mActivity.supportFragmentManager, "dialog")
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
        //TODO:Using session for image, name, type.
        val newPost = Post(
            "https://res.cloudinary.com/practicaldev/image/fetch/s--XNLLovS3--/c_fill,f_auto,fl_progressive,h_320,q_auto,w_320/https://thepracticaldev.s3.amazonaws.com/uploads/user/profile_image/140924/29f64b51-f6d2-4fbe-9b16-f143b55e1949.jpeg",
            "Darren God",
            "Walker",
            title,
            content
        )

        val listPost = postViewModel.getAllPost().plus(newPost)

        postAdapter.list = listPost
        postAdapter.notifyDataSetChanged()
    }
}