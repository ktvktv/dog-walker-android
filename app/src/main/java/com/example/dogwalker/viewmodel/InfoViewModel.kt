package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.*
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception

class InfoViewModel : ViewModel() {

    private val TAG = InfoViewModel::class.java.simpleName
    val infoResponse = MutableLiveData<CommonResponse>()
    val dogResponse = MutableLiveData<DogResponse>()

    suspend fun getInformation(session: String) {
        infoResponse.value = getUserDetail(session)
        dogResponse.value = getDogDetail(session)
    }

    suspend fun getUserInformation(session: String) {
        infoResponse.value = getUserDetail(session)
    }

    private suspend fun getUserDetail(session: String): CommonResponse?
            = withContext(Dispatchers.IO) {
        var resp: CommonResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.getUserInformation(session)?.await()
        } catch (e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }
        Log.d(TAG, "$resp")

        resp
    }

    suspend fun getDogInformation(session: String) {
        dogResponse.value = getDogDetail(session)
    }

    private suspend fun getDogDetail(session: String): DogResponse?
            = withContext(Dispatchers.IO) {
        var resp: DogResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.getDogInformation(session)?.await()
        } catch (e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }
        Log.d(TAG, "$resp")

        resp
    }

    suspend fun uploadImage(file: File, session: String) {
        infoResponse.value = sendImage(file, session)

        Log.d(TAG, "${infoResponse.value}")
    }

    private suspend fun sendImage(file: File, session: String) : CommonResponse?
            = withContext(Dispatchers.IO) {
        var resp: CommonResponse? = null
        try {
            val fileReqBody = RequestBody.create(MediaType.parse("image/*"), file)
            val photo = MultipartBody.Part.createFormData("photo", file.name, fileReqBody)
            resp = DogWalkerServiceApi.DogWalkerService.updateUserInformation(
                session = session,
                photo = photo)!!.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }

        resp
    }

    suspend fun updateToken(session: String, token: String) {
        var resp: CommonResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.updateUserInformation(
                session = session,
                token = token
            )?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }

        resp
    }
}