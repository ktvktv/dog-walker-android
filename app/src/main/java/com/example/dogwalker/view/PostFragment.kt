package com.example.dogwalker.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalker.SinglePost
import com.example.dogwalker.adapter.PostViewAdapter
import com.example.dogwalker.data.Comment
import com.example.dogwalker.data.Post
import com.example.dogwalker.databinding.FragmentPostBinding

class PostFragment : Fragment(), PostViewAdapter.PostViewAdapterClickListener {

    private lateinit var binding: FragmentPostBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPostBinding.inflate(inflater)

        Log.d("onCreateView", "ON CREATE CALLED")

        val listData = listOf(Post(
            "https://res.cloudinary.com/practicaldev/image/fetch/s--XNLLovS3--/c_fill,f_auto,fl_progressive,h_320,q_auto,w_320/https://thepracticaldev.s3.amazonaws.com/uploads/user/profile_image/140924/29f64b51-f6d2-4fbe-9b16-f143b55e1949.jpeg",
            "Darren God",
            "Walker",
            "Why am I so God",
            "I'm God bitches.",
            listOf(
                Comment("https://pbs.twimg.com/profile_images/378800000110177275/c441ab64d2e233d63eeed78d5b116571_400x400.jpeg","Kevin", "That's true though", "15 November 2019 14:09:00"),
                Comment("https://pbs.twimg.com/profile_images/378800000110177275/c441ab64d2e233d63eeed78d5b116571_400x400.jpeg","Kevin", "That's true though", "15 November 2019 14:09:00")
            )),
            Post("https://res.cloudinary.com/practicaldev/image/fetch/s--XNLLovS3--/c_fill,f_auto,fl_progressive,h_320,q_auto,w_320/https://thepracticaldev.s3.amazonaws.com/uploads/user/profile_image/140924/29f64b51-f6d2-4fbe-9b16-f143b55e1949.jpeg",
                "Darren so God", "Customer", "Please my God", "My godness power overflowing",
                listOf(Comment("https://cbbinusblog.files.wordpress.com/2017/05/14946023667391.jpg","Edward", "Absolutely madlad", "15 November 2019 14:09:00")))
            , Post(
                "https://res.cloudinary.com/practicaldev/image/fetch/s--XNLLovS3--/c_fill,f_auto,fl_progressive,h_320,q_auto,w_320/https://thepracticaldev.s3.amazonaws.com/uploads/user/profile_image/140924/29f64b51-f6d2-4fbe-9b16-f143b55e1949.jpeg",
                "Darren God",
                "Walker",
                "Why am I so God",
                "I'm God bitches.",
                listOf(
                    Comment("https://pbs.twimg.com/profile_images/378800000110177275/c441ab64d2e233d63eeed78d5b116571_400x400.jpeg","Kevin", "That's true though", "15 November 2019 14:09:00"),
                    Comment("https://pbs.twimg.com/profile_images/378800000110177275/c441ab64d2e233d63eeed78d5b116571_400x400.jpeg","Kevin", "That's true though", "15 November 2019 14:09:00")
                ))
        )
        binding.postRecyclerView.adapter = PostViewAdapter(listData, this)
        binding.postRecyclerView.layoutManager = LinearLayoutManager(context)

        return binding.root
    }

    override fun postViewClickListener(position: Int) {
        val intent = Intent(context, SinglePost::class.java)

        intent.putExtra("PostFragment", position)

        startActivity(intent)
    }
}