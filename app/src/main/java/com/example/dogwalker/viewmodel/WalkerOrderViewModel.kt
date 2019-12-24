package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.LOGIN_SUCCESSFUL
import com.example.dogwalker.data.Dog
import com.example.dogwalker.data.DogResponse
import com.example.dogwalker.network.DogWalkerServiceApi
import java.lang.Exception

class WalkerOrderViewModel : ViewModel() {
    private val TAG = WalkerOrderViewModel::class.java.simpleName
    val listDog = MutableLiveData<List<Dog>>()
    val walkerOrderMessage = MutableLiveData<String>()

    suspend fun GetDog(session: String) {
        var dogResponse: DogResponse?
        try {
            dogResponse = DogWalkerServiceApi.DogWalkerService.getDogInformation(session)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            walkerOrderMessage.value = "Unknown error, please try again"
            return
        }

        if(dogResponse != null) {
            if(dogResponse.message == LOGIN_SUCCESSFUL) {
                listDog.value = dogResponse.body
                return
            }

            walkerOrderMessage.value = dogResponse.message
            return
        }

        walkerOrderMessage.value = "Unknown error, please try again"
    }
}