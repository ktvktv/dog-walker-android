package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.RateResponse
import com.example.dogwalker.data.RatingRequest
import com.example.dogwalker.network.DogWalkerServiceApi
import java.lang.Exception

class DashboardViewModel : ViewModel() {

    private val TAG = DashboardViewModel::class.java.simpleName
    val transactionResponse = MutableLiveData<RateResponse>()

    suspend fun rateWalker(session: String, ratingRequest: RatingRequest) {
        try {
            DogWalkerServiceApi.DogWalkerService.rateWalker(session, ratingRequest)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }
    }
}