package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.RateResponse
import com.example.dogwalker.data.RatingRequest
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class DashboardViewModel : ViewModel() {

    private val TAG = DashboardViewModel::class.java.simpleName
    val transactionResponse = MutableLiveData<RateResponse>()
    val ratingResponse = MutableLiveData<RateResponse>()

    suspend fun rateWalker(session: String, ratingRequest: RatingRequest) {
        ratingResponse.value = ratingWalker(session, ratingRequest)
    }

    private suspend fun ratingWalker(session: String, ratingRequest: RatingRequest) : RateResponse?
            = withContext(Dispatchers.IO) {
        var resp: RateResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.rateWalker(session, ratingRequest)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }

        resp
    }

    suspend fun getTransaction(session: String) {
        transactionResponse.value = getTransactionBackground(session)
    }

    private suspend fun getTransactionBackground(session: String) : RateResponse?
            = withContext(Dispatchers.IO) {
        var resp: RateResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.getLatestTransaction(session)?.await()
        } catch (e: Exception) {
            Log.e(TAG, "Transaction error: ${e.message}")
            e.printStackTrace()
        }

        resp
    }
}