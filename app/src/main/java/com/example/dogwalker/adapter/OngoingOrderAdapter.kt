package com.example.dogwalker.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dogwalker.R
import com.example.dogwalker.data.NotifyData
import com.example.dogwalker.data.Order
import kotlinx.android.synthetic.main.ongoing_order_item.view.*

class OngoingOrderAdapter(val userType: String, val listOrder: List<Order>,
                          val ongoingClickListener: OngoingClickListener,
                          val pendingClickListener: PendingClickListener
) : RecyclerView.Adapter<OngoingOrderAdapter.ViewHolder>() {

    private val TAG = OngoingOrderAdapter::class.java.simpleName

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.ongoing_order_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.name_ongoing_order.text = listOrder[position].name
        holder.itemView.status_ongoing_order.text = listOrder[position].status
        holder.itemView.date_ongoing_order.text = listOrder[position].date

        if(!userType.equals("Walker")) {
            holder.itemView.address_text_view.visibility = View.GONE
        } else {
            holder.itemView.address_text_view.text = listOrder[position].address
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            Log.d(TAG, "view holder position: $layoutPosition")
            //Using shared preferences to store the data.
            var phone = "081293312313"

            val order = listOrder[layoutPosition]
            when {
                order.status == "On Going" -> {
                    if(this@OngoingOrderAdapter.userType.equals("Customer")) {
                        phone = listOrder[layoutPosition].phone
                    }

                    ongoingClickListener.onClick(
                        phone,
                        this@OngoingOrderAdapter.userType
                    )
                }
                order.status == "Pending" -> {
                    pendingClickListener.pendingClick(NotifyData(
                        order.userImageUrl,
                        "${order.name} want you to walk ${order.name}'s dog",
                        order.date
                    ))
                }
                else -> Toast.makeText(v!!.context, "Tracking only for current order", Toast.LENGTH_SHORT).show()
            }
        }

    }

    interface OngoingClickListener {
        fun onClick(phone: String, walkerType: String)
    }

    interface PendingClickListener {
        fun pendingClick(pendingData: NotifyData)
    }
}