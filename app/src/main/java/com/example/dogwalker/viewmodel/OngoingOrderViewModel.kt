package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.data.ListOrderResponse
import com.example.dogwalker.data.Order
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class OngoingOrderViewModel : ViewModel() {
    private val TAG = OngoingOrderViewModel::class.java.simpleName
    val orderList = MutableLiveData<List<Order>>()

    suspend fun getListOrder(session: String) {
        val orderResponse = fetchListOrderCustomer(session)

        if(orderResponse != null) {
            if(orderResponse.message == SUCCESSFUL) {
                orderList.value = orderResponse.body
            }
        }
    }

    private suspend fun fetchListOrderCustomer(session: String): ListOrderResponse?
            = withContext(Dispatchers.IO) {
        var orderResponse: ListOrderResponse? = null
        try {
            orderResponse = DogWalkerServiceApi.DogWalkerService.getListOrderCustomer(session)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }

        orderResponse
    }

    suspend fun getWalkerListOrder(session: String) {
        val orderResponse = fetchWalkerListOrder(session)

        if(orderResponse != null) {
            if(orderResponse.message == SUCCESSFUL) {
                orderList.value = orderResponse.body
            }
        }
    }

    private suspend fun fetchWalkerListOrder(session: String) : ListOrderResponse?
            = withContext(Dispatchers.IO) {
        var orderResponse: ListOrderResponse? = null
        try {
            orderResponse = DogWalkerServiceApi.DogWalkerService.getWalkerListOrder(session)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }

        orderResponse
    }
}