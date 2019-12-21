package com.example.dogwalker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.dogwalker.R
import com.example.dogwalker.data.NotifyData
import kotlinx.android.synthetic.main.fragment_order_decision.view.*
import kotlinx.android.synthetic.main.info_item.view.*

class OrderDecisionFragment(val notifyData: NotifyData) : DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_decision, container)

        if(notifyData.userImageUrl != null && notifyData.userImageUrl != "") {
            val imgUri = notifyData.userImageUrl.toUri().buildUpon().scheme("https").build()
            val imageView = view.user_image

            Glide.with(imageView.context)
                .load(imgUri)
                .into(imageView)
        }

        view.description_text.text = notifyData.description
        view.date_text.text = notifyData.date

        view.yes_button.setOnClickListener {
            //Call API
        }

        view.no_button.setOnClickListener {
            //Call API
        }

        return view
    }
}