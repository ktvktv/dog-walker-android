package com.example.dogwalker.viewmodel

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.LOGIN_SUCCESSFUL
import com.example.dogwalker.R
import com.example.dogwalker.data.*
import com.example.dogwalker.network.DogWalkerServiceApi
import com.example.dogwalker.view.InfoFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.lang.Exception

class InfoViewModel() : ViewModel() {

    private val TAG = InfoViewModel::class.java.simpleName
    val infoResponse = MutableLiveData<LoginResponse>()
//    val dogResponse = MutableLiveData<>

    suspend fun getUserInformation(session: String) {
        try {
            infoResponse.value = DogWalkerServiceApi.DogWalkerService.getUserInformation(session)?.await()
        } catch (e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }
    }

//    suspend fun

//    fun getUserInformation(): User? {
//        return _user.value!!
//    }

    private suspend fun hitAPI(file: File) {

//        val dogsData = DogRequest(
//            ownerId = 1,
//            breedId = 1,
//            age = 1,
//            weight = 1,
//            specialNeeds = "1",
//            name = "1",
//            gender = "1",
//            photo = RequestBody.create(MultipartBody.FORM, file)
//        )

//        val dogData = MultipartBody.Builder()
//            .addFormDataPart("owner_id", "1")
//            .addFormDataPart("breed_id", "1")
//            .addFormDataPart("age", "1")
//            .addFormDataPart("weight", "1")
//            .addFormDataPart("special_needs", "1")
//            .addFormDataPart("name", "1")
//            .addFormDataPart("gender", "1")
//            .addFormDataPart("photo", "dog.jpg", RequestBody.create(MultipartBody.FORM, file))
//            .setType(MultipartBody.FORM)
//            .build()

        var result: CommonResponse? = null
        try {
            result = DogWalkerServiceApi.DogWalkerService.registerDog(ownerId = 1,
                breedId = 1,
                age = 1,
                weight = 1,
                specialNeeds = "1",
                name = "1",
                gender = "1",
                photo = RequestBody.create(MultipartBody.FORM, file))!!.await()
        } catch(e: Exception) {
            Log.e("hitAPI", e.message)
            e.printStackTrace()
            return
        }

        Log.d("hitAPI", result.toString())
    }
}