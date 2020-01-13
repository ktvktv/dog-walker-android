package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.data.*
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlin.Exception

class OrderDetailViewModel : ViewModel() {
    private val TAG = WalkerInfoViewModel::class.java.simpleName
    val walkerInfoData = MutableLiveData<Walker>()
    val dogResponse = MutableLiveData<SoloDogResponse>()
    val customerResponse = MutableLiveData<CommonResponse>()
    val orderResponse = MutableLiveData<OrderResponse>()
    var walkerErrorMessage: String? = null

    suspend fun getWalkerData(session: String, walkerId: Int) {
        var walkerResponse: WalkerResponse?
        try {
            walkerResponse = DogWalkerServiceApi.DogWalkerService.getWalkerData(session, walkerId.toString())?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            walkerErrorMessage = "Unknown error, please try again"
            return
        }

        Log.d(TAG, walkerResponse.toString())

        if(walkerResponse != null) {
            if(walkerResponse.message == SUCCESSFUL && walkerResponse.body != null) {
                walkerInfoData.value = walkerResponse.body
                return
            }

            walkerErrorMessage = walkerResponse.message
        }

        walkerErrorMessage = "Unknown error, please try again"
    }

    suspend fun getDogInformation(session: String, dogId: Int) {
        Log.d(TAG, "$dogId")
        try {
            dogResponse.value = DogWalkerServiceApi.DogWalkerService.getDog(session, dogId)?.await()
        } catch (e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }

        Log.d(TAG, "${dogResponse.value}")
    }

    suspend fun PostOrder(session: String, order: PostOrderRequest) {
        try {
            orderResponse.value = DogWalkerServiceApi.DogWalkerService.postOrder(session, order)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }
    }

    suspend fun getCustomerData(session: String, customerId: Int) {
        try {
            customerResponse.value = DogWalkerServiceApi.DogWalkerService.getUser(session, customerId)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }
        Log.d(TAG, customerResponse.value.toString())
    }

    suspend fun SendNotification(session: String, notification: Notification) {
        var commonResponse: CommonResponse? = null
        try {
            commonResponse = DogWalkerServiceApi.DogWalkerService.sendNotification(session, notification)!!.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }

        if(commonResponse != null){
            //TODO:Fix this if there's time
        }
    }
}