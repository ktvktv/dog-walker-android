package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.data.LoginRequest
import com.example.dogwalker.data.LoginResponse
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    private val TAG = LoginViewModel::class.java.simpleName
    val isLoginSuccess = MutableLiveData<Boolean>()
    val loginResponse = MutableLiveData<LoginResponse>()

    suspend fun checkCredential(loginData: LoginRequest) {
        loginResponse.value = checkCredentialBackgroundTask(loginData)

        Log.i(TAG, "Login response: ${loginResponse.value}}")

        if (loginResponse.value != null) {
            if (loginResponse.value!!.message.equals(SUCCESSFUL)) {
                isLoginSuccess.value = true
                return
            }
        }

        isLoginSuccess.value = false
    }

    private suspend fun checkCredentialBackgroundTask(loginData: LoginRequest) : LoginResponse?
            = withContext(Dispatchers.IO) {
        var loginResponse : LoginResponse? = null
        try {
            loginResponse = DogWalkerServiceApi.DogWalkerService.login(loginData)?.await()
        } catch (e: Exception) {
            Log.e(TAG, "Login error: ${e.message}")
        }

        loginResponse
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