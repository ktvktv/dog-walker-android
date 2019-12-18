package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.LOGIN_SUCCESSFUL
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.LoginRequest
import com.example.dogwalker.data.LoginResponse
import com.example.dogwalker.data.User
import com.example.dogwalker.network.DogWalkerServiceApi

class LoginViewModel : ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName
    val isLoginSuccess = MutableLiveData<Boolean>()
    val loginResponse = MutableLiveData<LoginResponse>()

    suspend fun checkCredential(loginData: LoginRequest) {
        try {
            loginResponse.value = DogWalkerServiceApi.DogWalkerService.login(loginData)?.await()
        } catch (e: Exception) {
            Log.e(TAG, "Login error: ${e.message}")
            isLoginSuccess.value = false
            return
        }

        Log.i(TAG, "Login response: ${loginResponse.value}}")
        if (loginResponse.value != null) {
            if (loginResponse.value!!.message.equals(LOGIN_SUCCESSFUL)) {
                isLoginSuccess.value = true
                return
            }
        }

        isLoginSuccess.value = false
    }

    fun getLoginMessage(): String {
        if(loginResponse.value == null || loginResponse.value!!.message == null) {
            return "Unknown message"
        }

        return loginResponse.value!!.message!!
    }

    fun getLoginResponse(): LoginResponse? {
        return loginResponse.value
    }
}