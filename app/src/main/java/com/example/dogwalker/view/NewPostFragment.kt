package com.example.dogwalker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.dogwalker.R
import com.example.dogwalker.data.Post
import kotlinx.android.synthetic.main.fragment_comment.view.*
import kotlinx.android.synthetic.main.fragment_comment.view.post_cancel_button
import kotlinx.android.synthetic.main.fragment_comment.view.post_content_text
import kotlinx.android.synthetic.main.fragment_comment.view.post_ok_button
import kotlinx.android.synthetic.main.fragment_new_post.view.*

class NewPostFragment(val postAddition: PostAddition, val isUpdate: Boolean,
                      val postId: Int): DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_post, container, false)

        if(isUpdate) {
            //Get data
        }

        view.post_ok_button.setOnClickListener {
            val textContent = view.post_content_text.text.toString()
            val title = view.title_post_text.text.toString()

            if (textContent.equals("")) {
                Toast.makeText(context, "Content should not be empty", Toast.LENGTH_SHORT).show()
            } else if(title.equals("")) {
                Toast.makeText(context, "Title should not be empty", Toast.LENGTH_SHORT).show()
            } else {
                if(isUpdate) {
                    postAddition.updateNewPost(textContent, title)
                } else {
                    postAddition.addNewPost(textContent, title)
                }
                dismiss()
            }
        }

        view.post_cancel_button.setOnClickListener {
            dismiss()
        }

        return view
    }

    interface PostAddition {
        fun addNewPost(content: String, title: String)
        fun updateNewPost(content: String, title: String)
    }
}