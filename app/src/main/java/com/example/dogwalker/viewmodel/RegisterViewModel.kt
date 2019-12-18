package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.LOGIN_SUCCESSFUL
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.LoginResponse
import com.example.dogwalker.data.Register
import com.example.dogwalker.network.DogWalkerServiceApi
import java.text.SimpleDateFormat

class RegisterViewModel : ViewModel() {

    val isRegisterSuccess = MutableLiveData<Boolean>()
    private val registerResponse = MutableLiveData<CommonResponse>()
    private val TAG = RegisterViewModel::class.java.simpleName

    suspend fun register(registerData: Register) {
        Log.i(TAG, "Register Data: $registerData")

        try {
            registerResponse.value = DogWalkerServiceApi.DogWalkerService.register(registerData)?.await()
        } catch(e: Exception) {
            Log.e(TAG, "Register error: ${e.message}")
            isRegisterSuccess.value = false
            return
        }

        Log.i(TAG, "Register response: ${registerResponse.value}")

        if(registerResponse.value?.message == LOGIN_SUCCESSFUL) {
            isRegisterSuccess.value = true
            return
        }

        isRegisterSuccess.value = false
    }

    fun getRegisterMessage() : CommonResponse? {
        return registerResponse.value
    }
}