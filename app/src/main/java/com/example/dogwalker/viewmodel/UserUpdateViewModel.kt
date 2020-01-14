package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class UserUpdateViewModel : ViewModel() {
    private val TAG = UserUpdateViewModel::class.java.simpleName
    val user = MutableLiveData<CommonResponse>()

    suspend fun getUserInfo(session: String) {
        user.value = getUserBackground(session)
    }

    private suspend fun getUserBackground(session: String) :CommonResponse? = withContext(Dispatchers.IO) {
        var resp: CommonResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.getUserInformation(session)?.await()
        } catch(e: Exception) {
            Log.d(TAG, "Get User Error: ${e.message}")
            e.printStackTrace()
        }

        resp
    }
}