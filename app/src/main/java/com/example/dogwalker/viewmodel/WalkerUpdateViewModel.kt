package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.WalkerUpdateRequest
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WalkerUpdateViewModel : ViewModel() {
    private val TAG = WalkerUpdateViewModel::class.java.simpleName
    val walkerUpdateResponse = MutableLiveData<CommonResponse>()

    suspend fun updateWalker(session: String, walkerData: WalkerUpdateRequest) {
        walkerUpdateResponse.value = updateWalkerBackground(session, walkerData)
    }

    private suspend fun updateWalkerBackground(session: String, walkerData: WalkerUpdateRequest) : CommonResponse?
            = withContext(Dispatchers.IO) {
        var resp: CommonResponse? = null

        try {
            resp = DogWalkerServiceApi.DogWalkerService.updateWalker(session, walkerData)?.await()
        } catch(e: Exception) {
            Log.e(TAG, "Walker Update Error: ${e.message}")
            e.printStackTrace()
        }

        resp
    }
}