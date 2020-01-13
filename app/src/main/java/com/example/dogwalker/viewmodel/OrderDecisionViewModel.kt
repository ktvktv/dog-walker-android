package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.*
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class OrderDecisionViewModel : ViewModel() {
    private val TAG = OrderDecisionViewModel::class.java.simpleName
    val transactionResponse = MutableLiveData<CommonResponse>()

    suspend fun changeStatus(session: String, transactionStatus: TransactionStatus) {
        transactionResponse.value = changeStatusBackground(session, transactionStatus)
    }

    private suspend fun changeStatusBackground(session: String, transactionStatus: TransactionStatus) : CommonResponse?
            = withContext(Dispatchers.IO) {
        var resp: CommonResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.changeTransactionStatus(session, transactionStatus)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }

        resp
    }

    suspend fun rejectTransaction(session: String, transactionId: Int) {
        Log.d(TAG, "TRANSACTION-ID: $transactionId")
        transactionResponse.value = rejectTransactionBackground(session, transactionId)
    }

    private suspend fun rejectTransactionBackground(session: String, transactionId: Int) : CommonResponse?
            = withContext(Dispatchers.IO) {
        var resp: CommonResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.rejectTransaction(session, transactionId)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }

        resp
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