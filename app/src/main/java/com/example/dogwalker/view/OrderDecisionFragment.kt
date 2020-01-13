package com.example.dogwalker.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.example.dogwalker.APPROVED
import com.example.dogwalker.R
import com.example.dogwalker.data.Notification
import com.example.dogwalker.data.NotifyData
import com.example.dogwalker.data.TransactionStatus
import com.example.dogwalker.receiver.AlarmReceiver
import com.example.dogwalker.viewmodel.OngoingOrderViewModel
import com.example.dogwalker.viewmodel.OrderDecisionViewModel
import com.example.dogwalker.viewmodel.ViewModelFactory
import kotlinx.android.synthetic.main.fragment_order_decision.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class OrderDecisionFragment(val notifyData: NotifyData, val ongoingOrderViewModel: OngoingOrderViewModel) : DialogFragment() {
    private val TAG = OrderDecisionFragment::class.java.simpleName
    private val orderDecisionViewModel by lazy {
        ViewModelProviders.of(this, ViewModelFactory()).get(OrderDecisionViewModel::class.java)
    }
    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_decision, container)

        val sharedPreferences = activity!!.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE)
        val session = sharedPreferences.getString(getString(R.string.session_cache), "")
        val type = sharedPreferences.getString(getString(R.string.type_cache), "")
        val name = sharedPreferences.getString(getString(R.string.name_cache), "")

        if(notifyData.userImageUrl != null && notifyData.userImageUrl != "") {
            val imgUri = notifyData.userImageUrl.toUri().buildUpon().scheme("https").build()
            val imageView = view.user_image

            Glide.with(imageView.context)
                .load(imgUri)
                .into(imageView)
        }

        view.description_text.text = notifyData.description
        view.date_text.text = notifyData.date

        view.ok_button.setOnClickListener {
            coroutineScope.launch {
                orderDecisionViewModel.changeStatus(
                    session, TransactionStatus(
                        notifyData.transactionId,
                        APPROVED
                    )
                )

                if(type.toLowerCase() == "customer") {
                    ongoingOrderViewModel.getListOrder(session)
                } else {
                    ongoingOrderViewModel.getWalkerListOrder(session)
                }

                val alarmMgr = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
                    intent.putExtra("title", "Time to walk the dog")
                    intent.putExtra("body", "Don't forget to walk the dog sir")
                    intent.putExtra("id", notifyData.transactionId)
                    PendingIntent.getBroadcast(context, 0, intent, 0)
                }

                val dates = SimpleDateFormat("HH:mm:ss dd/MM/yyyy").parse(notifyData.date)

                val calendar: Calendar = Calendar.getInstance().apply {
                    timeInMillis = dates.time
                }

                Log.d(TAG, "Set alarm at TimesInMillis: ${calendar.timeInMillis}")
                alarmMgr.set(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    alarmIntent
                )

                orderDecisionViewModel.SendNotification(session, Notification(
                    "Order Accepted",
                    "$name accepted your order at ${notifyData.date}",
                    "Walker",
                    "",
                    notifyData.transactionId
                ))


                dismiss()
            }


        }

        view.no_button.setOnClickListener {
            coroutineScope.launch {
                orderDecisionViewModel.rejectTransaction(session, notifyData.transactionId)

                if(type.toLowerCase() == "customer") {
                    ongoingOrderViewModel.getListOrder(session)
                } else {
                    ongoingOrderViewModel.getWalkerListOrder(session)
                }

                orderDecisionViewModel.SendNotification(session, Notification(
                    "Order Rejected",
                    "$name rejected your order at ${notifyData.date}",
                    "Walker",
                    "",
                    notifyData.transactionId
                ))

                dismiss()
            }
        }

        return view
    }
}