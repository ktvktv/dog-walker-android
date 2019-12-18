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
import com.example.dogwalker.R
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.Dog
import com.example.dogwalker.data.DogRequest
import com.example.dogwalker.data.User
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
    private val _user = MutableLiveData<User>()

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    val user: LiveData<User>
        get() = _user

    init {
        //TODO:Network operation for info data.

        //Dummy data
//        _user.value = User(
//            name = "Kevin Tigravictor",
//            password = "Test",
//            email = "kevin.victor30@yahoo.com",
//            phoneNumber = "+6281290001998",
//            gender = "Male",
//            address = "Jl. Barleria VI B1/H5",
//            birthDate = "09 September 1998",
//            birthPlace = "DKI Jakarta",
//            userImageUrl = "https://pbs.twimg.com/profile_images/378800000110177275/c441ab64d2e233d63eeed78d5b116571_400x400.jpeg"
////            dog = listOf(
////                Dog(
////                    ownerId = 1,
////                    breedId = 1,
////                    age = 10,
////                    weight = 100,
////                    gender = "Male",
////                    name = "Willy",
////                    specialNeeds = "",
////                    photo = "https://pbs.twimg.com/profile_images/378800000110177275/c441ab64d2e233d63eeed78d5b116571_400x400.jpeg"),
////                Dog(
////                    ownerId = 2,
////                    breedId = 2,
////                    age = 20,
////                    weight = 150,
////                    gender = "Female",
////                    name = "Darren",
////                    specialNeeds = "",
////                    photo = "https://pbs.twimg.com/profile_images/378800000110177275/c441ab64d2e233d63eeed78d5b116571_400x400.jpeg")
////            )
//        )
    }

    fun getInfo() :User {
        return _user.value!!
    }

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