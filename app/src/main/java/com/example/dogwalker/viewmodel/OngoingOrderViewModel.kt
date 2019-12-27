package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.data.ListOrderResponse
import com.example.dogwalker.data.Order
import com.example.dogwalker.network.DogWalkerServiceApi
import java.lang.Exception

class OngoingOrderViewModel : ViewModel() {
    private val TAG = OngoingOrderViewModel::class.java.simpleName
    val orderList = MutableLiveData<List<Order>>()

    suspend fun getListOrder(session: String) {
        var orderResponse: ListOrderResponse?
        try {
            orderResponse = DogWalkerServiceApi.DogWalkerService.getListOrderCustomer(session)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }

        if(orderResponse != null) {
            if(orderResponse.message == SUCCESSFUL) {
                orderList.value = orderResponse.body
            }
        }
    }

    suspend fun getWalkerListOrder(session: String) {
        var orderResponse: ListOrderResponse?
        try {
            orderResponse = DogWalkerServiceApi.DogWalkerService.getWalkerListOrder(session)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }

        if(orderResponse != null) {
            if(orderResponse.message == SUCCESSFUL) {
                orderList.value = orderResponse.body
            }
        }
    }

    fun getOrderList() : List<Order> {
        if(orderList.value == null) {
            return listOf()
        }

        return orderList.value!!
    }
}