package com.example.dogwalker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.dogwalker.R
import kotlinx.android.synthetic.main.fragment_comment.view.*

class CommentFragment(val commentAddition: CommentAddition,
                      val postId: Int) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_comment, container, false)

        view.post_ok_button.setOnClickListener {
            val textContent = view.post_content_text.text.toString()

            if(textContent.equals("")) {
                Toast.makeText(context, "Komentar tidak boleh kosong!", Toast.LENGTH_SHORT).show()
            } else {
                commentAddition.addNewComment(textContent, postId)
                dismiss()
            }
        }

        view.post_cancel_button.setOnClickListener {
            dismiss()
        }

        return view
    }

    interface CommentAddition {
        fun addNewComment(comment: String, postId: Int)
    }
}