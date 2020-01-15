package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.DogResponse
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class DogUpdateViewModel : ViewModel() {
    private val TAG = DogUpdateViewModel::class.java.simpleName
    val listDogResponse = MutableLiveData<DogResponse>()

    suspend fun getListDog(session: String) {
        listDogResponse.value = getAllDog(session)
    }

    private suspend fun getAllDog(session: String) : DogResponse?
            = withContext(Dispatchers.IO) {
        var dogResponse : DogResponse? = null
        try {
            dogResponse = DogWalkerServiceApi.DogWalkerService.getDogInformation(session)?.await()
        } catch(e: Exception) {
            Log.e(TAG, "Getting all dog error: ${e.message}")
            e.printStackTrace()
        }

        dogResponse
    }
}