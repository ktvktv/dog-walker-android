package com.example.dogwalker.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.dogwalker.R
import com.example.dogwalker.databinding.FragmentDetailOrderBinding
import com.example.dogwalker.viewmodel.OrderDetailViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.info_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderDetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailOrderBinding
    val args: OrderDetailFragmentArgs by navArgs()
    private val orderDetailView by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(OrderDetailViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailOrderBinding.inflate(inflater)

        val sharedPreferences = context!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        val session = sharedPreferences.getString(getString(R.string.session_cache), "")

        coroutineScope.launch {
            orderDetailView.getWalkerData(session, args.walkerId)
            orderDetailView.getDogInformation(session, args.dogId)
        }

        orderDetailView.dogResponse.observe(this, Observer {
            if(it?.body != null) {
                val data = it.body
                binding.dogNameDetail.text = data.name

                if(data.photo != null) {
                    val imgUri = data.photo!!.toUri().buildUpon().scheme("https").build()
                    val imageView = binding.dogPictureDetail

                    Glide.with(imageView.context)
                        .load(imgUri)
                        .into(imageView)
                }
            }
        })

        orderDetailView.walkerInfoData.observe(this, Observer {
            if(it != null) {
                binding.walkerNameDetail.text = it.name

                if(it.photo != null) {
                    val imgUri = it.photo!!.toUri().buildUpon().scheme("https").build()
                    val imageView = binding.walkerPictureDetail

                    Glide.with(imageView.context)
                        .load(imgUri)
                        .into(imageView)
                }
            }
        })

        binding.addressDetailOrder.text = sharedPreferences.getString(getString(R.string.address_cache), "")
        binding.dateOrderText.text = args.date
        binding.totalPriceText.text = args.totalPrice.toString()

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}