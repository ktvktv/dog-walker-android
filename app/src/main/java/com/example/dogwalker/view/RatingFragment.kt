package com.example.dogwalker.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.dogwalker.R
import com.example.dogwalker.data.Order
import com.example.dogwalker.data.RatingRequest
import com.example.dogwalker.viewmodel.DashboardViewModel
import kotlinx.android.synthetic.main.fragment_rating.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RatingFragment(val order: Order, val dashboardViewModel: DashboardViewModel) : DialogFragment() {
    private val TAG = RatingFragment::class.java.simpleName
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rating, container, false)

        view.description_text.text = order.name
        view.date_text.text = order.walkDate

        if(order.photo != null && order.photo != "") {
            val imgUri = order.photo.toUri().buildUpon().scheme("https").build()
            val imageView = view.user_image

            Glide.with(imageView.context)
                .load(imgUri)
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .into(imageView)
        }

        var rates = 0

        view.ratingBar.setOnRatingBarChangeListener{ ratingBar, rate, user ->
            if(user) {
                rates = rate.toInt()
            }
        }

        view.ok_button.setOnClickListener {
//            if(rate == 0.0) {
//                Toast.makeText(context, "Please rate the walker", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            val session = context!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
                .getString(getString(R.string.session_cache), "")

            Log.d(TAG, "Rating: ${rates.toInt()}")

            coroutineScope.launch {
                dashboardViewModel.rateWalker(
                    session, RatingRequest(
                        order.clientId,
                        rates.toInt(),
                        order.id
                    )
                )
            }

            dismiss()
        }

        return view
    }
}