package com.example.dogwalker.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dogwalker.R
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.data.Post
import com.example.dogwalker.viewmodel.NewPostViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_comment.view.*
import kotlinx.android.synthetic.main.fragment_comment.view.post_cancel_button
import kotlinx.android.synthetic.main.fragment_comment.view.post_content_text
import kotlinx.android.synthetic.main.fragment_comment.view.post_ok_button
import kotlinx.android.synthetic.main.fragment_new_post.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewPostFragment(val postAddition: PostAddition, val isUpdate: Boolean,
                      val postId: Int): DialogFragment() {

    private val newPostViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(NewPostViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_new_post, container, false)

        val session = context!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
            .getString(getString(R.string.session_cache), "")
        if(isUpdate) {
            //Get data
            coroutineScope.launch {
                newPostViewModel.getPost(session, postId)
            }
        }

        newPostViewModel.postResponse.observe(this, Observer {
            if(it?.body != null && it.message == SUCCESSFUL) {
                view.post_content_text.setText(it.body.content)
                view.title_post_text.setText(it.body.title)
            }
        })

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