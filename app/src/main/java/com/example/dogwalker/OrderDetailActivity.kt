package com.example.dogwalker

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.dogwalker.data.Notification
import com.example.dogwalker.data.PostOrderRequest
import com.example.dogwalker.databinding.FragmentDetailOrderBinding
import com.example.dogwalker.viewmodel.OrderDetailViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class OrderDetailActivity : AppCompatActivity() {

    private val TAG = OrderDetailActivity::class.java.simpleName
    private lateinit var binding: FragmentDetailOrderBinding
    private val orderDetailView by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(OrderDetailViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        Log.d(TAG, "ORDER DETAIL FRAGMENT")
        binding = FragmentDetailOrderBinding.inflate(LayoutInflater.from(this))

        val sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        val session = sharedPreferences.getString(getString(R.string.session_cache), "")

        val clientId = intent.extras.getInt("clientId")
        val dogId = intent.extras.getInt("dogId")
        val hours = intent.extras.getInt("hours")
        val date = intent.extras.getString("date")
        val isOrder = intent.extras.getBoolean("isOrder")

        val type = sharedPreferences.getString(getString(R.string.type_cache), "")

        coroutineScope.launch {
            if(type.toLowerCase() == "customer") {
                orderDetailView.getWalkerData(session, clientId)
            } else {
                orderDetailView.getCustomerData(session, clientId)
            }
            orderDetailView.getDogInformation(session, dogId)
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

        if(type.toLowerCase() == "customer") {
            orderDetailView.walkerInfoData.observe(this, Observer {
                if (it != null) {
                    binding.walkerNameDetail.text = it.name

                    if (it.photo != null) {
                        val imgUri = it.photo!!.toUri().buildUpon().scheme("https").build()
                        val imageView = binding.walkerPictureDetail

                        Glide.with(imageView.context)
                            .load(imgUri)
                            .into(imageView)
                    }

                    binding.addressDetailOrder.text = it.address
                    binding.totalPriceText.text = "Rp. ${it.pricing * hours}"
                }
            })
        } else {
            orderDetailView.customerResponse.observe(this, Observer{
                if(it != null && it.message == SUCCESSFUL) {
                    binding.walkerNameDetail.text = it.body?.name

                    if (it.body?.userImageUrl != null) {
                        val imgUri = it.body?.userImageUrl!!.toUri().buildUpon().scheme("https").build()
                        val imageView = binding.walkerPictureDetail

                        Glide.with(imageView.context)
                            .load(imgUri)
                            .into(imageView)
                    }

                    binding.addressDetailOrder.text = it.body?.address
                }
            })

            val pricing = intent.extras.getInt("price")
            binding.totalPriceText.text = "Rp. ${pricing}"
        }

        if(isOrder) {
            binding.orderDetailButton.setOnClickListener {
                coroutineScope.launch {
                    orderDetailView.PostOrder(
                        session, PostOrderRequest(
                            dogId,
                            hours,
                            date,
                            clientId
                        )
                    )
                }
            }
        } else {
            binding.orderDetailButton.visibility = View.GONE
        }

        orderDetailView.orderResponse.observe(this, Observer {
            if(it != null && it.message == SUCCESSFUL) {
                    val orderId = it.body.id

                    coroutineScope.launch{
                        orderDetailView.SendNotification(session, Notification(
                                "Order incoming",
                                "Good news, there's a new order for you.",
                                "Customer",
                                "Good news, there's a new order for you.",
                                orderId
                            )
                        )
                    }

                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
                }
        })

        binding.dateOrderText.text = date
        binding.hourText.text = "$hours Hour"

        setContentView(binding.root)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == android.R.id.home) {
            finish()

            return true
        }

        return super.onOptionsItemSelected(item)
    }
}