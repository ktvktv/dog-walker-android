package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.MapsDetail
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class MapsViewModel : ViewModel(){
    private val TAG = MapsViewModel::class.java.simpleName
    val detailData = MutableLiveData<MapsDetail>()

    suspend fun getDetailData(session: String, id: Int) {
        detailData.value = getData(session, id)
    }

    private suspend fun getData(session: String, id: Int) : MapsDetail?
            = withContext(Dispatchers.IO) {
        var data :MapsDetail? = null
        try {
            data = DogWalkerServiceApi.DogWalkerService.getDetailTransaction(session, id)!!.await()
        } catch(e: Exception) {
            Log.e(TAG, "Error when get transaction detail, err: ${e.message}")
            e.printStackTrace()
        }

        data
    }
}