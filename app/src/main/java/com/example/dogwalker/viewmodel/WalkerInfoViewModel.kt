package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.LOGIN_SUCCESSFUL
import com.example.dogwalker.data.Walker
import com.example.dogwalker.data.WalkerResponse
import com.example.dogwalker.network.DogWalkerServiceApi
import java.lang.Exception

class WalkerInfoViewModel : ViewModel() {
    private val TAG = WalkerInfoViewModel::class.java.simpleName
    val walkerInfoData = MutableLiveData<Walker>()
    var walkerErrorMessage: String? = null

    suspend fun getWalkerData(session: String) {
        var walkerResponse: WalkerResponse?
        try {
            walkerResponse = DogWalkerServiceApi.DogWalkerService.getWalkerData(session)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            walkerErrorMessage = "Unknown error, please try again"
            return
        }

        if(walkerResponse != null) {
            if(walkerResponse.message == LOGIN_SUCCESSFUL && walkerResponse.body != null) {
                walkerInfoData.value = walkerResponse.body
                return
            }

            walkerErrorMessage = walkerResponse.message
        }

        walkerErrorMessage = "Unknown error, please try again"
    }
}