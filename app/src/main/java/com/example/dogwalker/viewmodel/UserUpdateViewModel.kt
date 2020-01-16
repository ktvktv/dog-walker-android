package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.User
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception

class UserUpdateViewModel : ViewModel() {
    private val TAG = UserUpdateViewModel::class.java.simpleName
    val user = MutableLiveData<CommonResponse>()
    val updateResp = MutableLiveData<CommonResponse>()

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

    suspend fun updateUserInfo(session: String, user: User, file: File?) {
        var photo: MultipartBody.Part? = null
        if(file != null) {
            val fileReqBody = RequestBody.create(MediaType.parse("image/*"), file)
            photo = MultipartBody.Part.createFormData("photo", file!!.name, fileReqBody)
        }

        updateResp.value = updateUser(session, user, photo)
    }

    private suspend fun updateUser(session: String, user: User, photo: MultipartBody.Part?) : CommonResponse?
        = withContext(Dispatchers.IO) {
        var resp: CommonResponse? = null

        try {
            resp = DogWalkerServiceApi.DogWalkerService.updateUserInformation(session,
                name = user.name,
                address = user.address,
                birthdate = user.birthDate,
                birthplace = user.birthPlace,
                email = user.email,
                gender = user.gender,
                nik = user.nik,
                phoneNumber = user.phoneNumber,
                password = user.password,
                photo = photo
                )?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }

        resp
    }
}