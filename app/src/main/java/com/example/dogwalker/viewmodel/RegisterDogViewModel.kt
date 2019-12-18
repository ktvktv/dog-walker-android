package com.example.dogwalker.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.adapter.BreedAdapter
import com.example.dogwalker.data.Breed
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.Dog
import com.example.dogwalker.data.RegisterDogRequest
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class RegisterDogViewModel : ViewModel() {

    private val TAG = RegisterDogViewModel::class.java.simpleName

    val listBreed = MutableLiveData<List<String>>()
    val registerResponse = MutableLiveData<CommonResponse>()

    private val coroutineScope = CoroutineScope(Job() + Dispatchers.Main)

    init {
        coroutineScope.launch {
            fetchBreedList()
        }
    }

    private suspend fun fetchBreedList() {
        var breedList: List<Breed>
        try {
            breedList = DogWalkerServiceApi.DogWalkerService.getAllBreed()!!.await().body
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }

        Log.d(TAG, "listBreed size: ${breedList.size}")

        val breedValue = ArrayList<String>()

        for(i in breedList) {
            breedValue.add(i.name)
        }

        listBreed.value = breedValue
    }

    suspend fun registerDog(registerDogData: RegisterDogRequest, file: File, session: String) {
        val fileReqBody = RequestBody.create(MediaType.parse("image/*"), file)
        val photo = MultipartBody.Part.createFormData("photo", file.name, fileReqBody)

        try {
            registerResponse.value =
                DogWalkerServiceApi.DogWalkerService.registerDog(
                    session = session,
                    breedId = registerDogData.breedId,
                    age = registerDogData.age,
                    weight = registerDogData.weight,
                    gender = registerDogData.gender,
                    name = registerDogData.name,
                    specialNeeds = registerDogData.specialNeeds,
                    photo = photo
                )!!.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }
    }
}