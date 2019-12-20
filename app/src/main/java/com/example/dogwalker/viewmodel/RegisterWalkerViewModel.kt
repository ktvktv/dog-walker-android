package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.RegisterWalkerRequest
import com.example.dogwalker.network.DogWalkerServiceApi
import java.lang.Exception

class RegisterWalkerViewModel : ViewModel() {
    private val TAG = RegisterWalkerViewModel::class.java.simpleName
    val registerWalkerResponse = MutableLiveData<CommonResponse>()

    suspend fun registerWalker(session: String, walkerData: RegisterWalkerRequest) {
        try {
            registerWalkerResponse.value = DogWalkerServiceApi.DogWalkerService.registerWalker(session, walkerData)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }
    }
}