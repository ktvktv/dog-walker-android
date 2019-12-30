package com.example.dogwalker.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.dogwalker.OrderDetailActivity
import com.example.dogwalker.R
import com.example.dogwalker.data.NotifyData
import com.example.dogwalker.data.Order
import kotlinx.android.synthetic.main.ongoing_order_item.view.*

class OngoingOrderAdapter(val userType: String, var listOrder: List<Order>,
                          val ongoingClickListener: OngoingClickListener,
                          val pendingClickListener: PendingClickListener,
                          val context: Context
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
        holder.itemView.status_ongoing_order_button.text = listOrder[position].status
        holder.itemView.date_ongoing_order.text = listOrder[position].walkDate

        holder.itemView.detail_button.setOnClickListener {
            val intent = Intent(context, OrderDetailActivity::class.java)
            intent.putExtra("dogId", listOrder[position].dogId)
            //TODO:Need changes
            intent.putExtra("clientId", listOrder[position].clientId)
            intent.putExtra("hours", listOrder[position].duration)
            intent.putExtra("date", listOrder[position].walkDate)
            intent.putExtra("price", listOrder[position].price)

            context.startActivity(intent)
        }

        holder.itemView.status_ongoing_order_button.setOnClickListener {
            val order = listOrder[position]
            when(it.status_ongoing_order_button.text.toString().toLowerCase()) {
                "OnGoing".toLowerCase() -> {
                    ongoingClickListener.onClick(
                        order.phoneNumber,
                        userType
                    )
                }

                //TODO:Modif this
                "Pending".toLowerCase() -> {
                    if(userType.toLowerCase() == "walker") {
                        pendingClickListener.pendingClick(
                            NotifyData(
                                order.id,
                                order.photo,
                                "${order.name} want you to walk ${order.name}'s dog",
                                order.walkDate,
                                "",
                                ""
                            )
                        )
                    }
                }

                else -> Toast.makeText(holder.itemView.context, "Tracking only for current order", Toast.LENGTH_SHORT).show()
            }
        }

        if(!userType.equals("Walker")) {
            holder.itemView.address_text_view.visibility = View.GONE
        } else {
            holder.itemView.address_text_view.text = listOrder[position].address
        }
    }

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    interface OngoingClickListener {
        fun onClick(phone: String, walkerType: String)
    }

    interface PendingClickListener {
        fun pendingClick(pendingData: NotifyData)
    }
}