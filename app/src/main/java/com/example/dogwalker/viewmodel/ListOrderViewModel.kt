package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.data.ListWalkerRequest
import com.example.dogwalker.data.ListWalkerResponse
import com.example.dogwalker.data.Walker
import com.example.dogwalker.network.DogWalkerServiceApi
import java.lang.Exception

class ListOrderViewModel : ViewModel() {
    private val TAG = ListOrderViewModel::class.java.simpleName

    val listWalker = MutableLiveData<List<Walker>>()
    val errorMessage = MutableLiveData<String>()

    suspend fun getFilteredWalker(session: String, listWalkerRequest: ListWalkerRequest) {
        Log.d(TAG, "$listWalkerRequest")
        var listWalkerResponse: ListWalkerResponse?
        try {
            listWalkerResponse = DogWalkerServiceApi.DogWalkerService.getFilteredWalker(session, listWalkerRequest)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()

            errorMessage.value = "Unknown error, please try again"
            return
        }

        Log.d(TAG, "$listWalkerResponse")

        if(listWalkerResponse != null) {
            if(listWalkerResponse.message == SUCCESSFUL) {
                listWalker.value = listWalkerResponse.body
                return
            }

            errorMessage.value = listWalkerResponse.message
            return
        }

        errorMessage.value = "Unknown error, please try again"
    }
}