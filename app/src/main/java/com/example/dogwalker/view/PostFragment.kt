package com.example.dogwalker.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dogwalker.SinglePostActivity
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
                "Darren so God", "Customer", "Please my God", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla felis risus, dapibus nec mauris in, egestas maximus elit. Praesent porttitor massa in magna placerat, sed tempor magna sagittis. Etiam condimentum varius arcu, sed congue est tempor et. Praesent sagittis suscipit mauris, mattis porttitor lectus tristique eget. Ut quis mi iaculis, ullamcorper urna non, posuere magna. Vestibulum ex ante, commodo vel erat quis, sodales euismod est. Pellentesque tincidunt posuere purus, sed vestibulum lorem posuere fringilla. Phasellus cursus, ligula aliquet lobortis rhoncus, arcu arcu dapibus nibh, a porta elit elit id nisl. Proin convallis eleifend lacus sed lobortis." +
                        "Morbi vitae luctus purus. Integer pulvinar nunc eu tristique vehicula. Maecenas dignissim, libero id ultrices efficitur, nibh mi vulputate mi, non elementum nisl massa ac felis. Etiam hendrerit, diam eu ullamcorper euismod, quam purus convallis nunc, hendrerit elementum odio odio sed enim. Nam mattis urna mi. Phasellus lacus sapien, molestie a tristique eget, mollis et ex. Pellentesque auctor leo vitae laoreet eleifend. Duis dapibus sem sed bibendum fringilla. Vivamus mauris nisl, tincidunt et varius eget, facilisis id lacus. Duis porttitor sapien vel velit pharetra, eget tempus lorem dignissim. Fusce venenatis purus at nisl vulputate imperdiet. Phasellus bibendum congue ligula, non dictum ex tincidunt id. Sed vulputate odio erat, quis viverra tellus egestas quis. Pellentesque facilisis vestibulum lacus, in egestas justo dapibus ac." +
                        "Donec nec malesuada nulla. Donec molestie, nunc et facilisis hendrerit, libero justo aliquam urna, vulputate gravida dolor lorem id leo. Ut eros elit, ullamcorper mollis faucibus sed, suscipit vel justo. Fusce rutrum, nunc in blandit bibendum, elit ipsum consectetur turpis, at placerat nunc neque quis nisi. Donec at sapien eu quam porta fringilla sit amet eget augue. Quisque ac dui eu quam egestas facilisis. Aliquam quis massa at tellus ornare sollicitudin. Suspendisse semper viverra diam et iaculis. Curabitur ac risus vitae ipsum gravida posuere. Nulla eget dolor in mi laoreet mattis at vitae ante. Sed tristique est non erat sagittis, eget eleifend libero semper. Nulla fermentum enim quis lorem viverra congue. In lacus odio, suscipit sit amet tincidunt quis, venenatis sodales felis. In eu dolor eu odio rhoncus dictum vitae in orci." +
                        "Aenean hendrerit ipsum quis diam congue, at mollis augue auctor. Nulla elementum ipsum vitae sem ultrices, vel consequat nunc dapibus. Maecenas faucibus nunc at massa sodales commodo. Nulla at tortor risus. Sed ullamcorper id enim vitae pulvinar. Ut in odio vel nibh mattis pellentesque. Praesent fermentum tortor turpis, finibus lacinia nisl commodo pulvinar. Nullam a est eget ante porta blandit. In ultricies varius faucibus. Nunc sagittis massa magna, eget ultrices est mattis ut. Proin iaculis non orci et finibus. Praesent et venenatis felis, ac auctor erat." +
                        "Phasellus a maximus nisi. Sed et dolor vitae tortor imperdiet suscipit sed quis nisl. Praesent sed ipsum arcu. Curabitur convallis vitae lectus in placerat. Nullam lobortis lacus massa, et porttitor erat vulputate sed. Quisque porttitor laoreet ultrices. Curabitur lacinia mi non dolor porta, ut ultrices dui volutpat. Nullam ut orci vitae ex aliquam porta.",
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
        val intent = Intent(context, SinglePostActivity::class.java)

        intent.putExtra("PostFragment", position)

        startActivity(intent)
    }
}