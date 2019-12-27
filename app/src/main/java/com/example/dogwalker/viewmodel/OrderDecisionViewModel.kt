package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.DogResponse
import com.example.dogwalker.data.TransactionStatus
import com.example.dogwalker.data.WalkerResponse
import com.example.dogwalker.network.DogWalkerServiceApi
import java.lang.Exception

class OrderDecisionViewModel : ViewModel() {
    private val TAG = OrderDecisionViewModel::class.java.simpleName
    val transactionResponse = MutableLiveData<CommonResponse>()

    suspend fun changeStatus(session: String, transactionStatus: TransactionStatus) {
        try {
            transactionResponse.value = DogWalkerServiceApi.DogWalkerService.changeTransactionStatus(session, transactionStatus)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            transactionResponse.value  = CommonResponse("Unknown error, please try again", null, 0, null)
            return
        }
    }

    suspend fun rejectTransaction(session: String, transactionId: Int) {
        Log.d(TAG, "TRANSACTION-ID: $transactionId")
        try {
            transactionResponse.value = DogWalkerServiceApi.DogWalkerService.rejectTransaction(session, transactionId)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            transactionResponse.value  = CommonResponse("Unknown error, please try again", null, 0, null)
            return
        }
    }
}