package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.*
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import java.io.File
import java.lang.Exception

class DogUpdateViewModel : ViewModel() {
    private val TAG = DogUpdateViewModel::class.java.simpleName
    val listDogResponse = MutableLiveData<DogResponse>()
    val updateDogResponse = MutableLiveData<CommonResponse>()
    val deleteDogResponse = MutableLiveData<CommonResponse>()
    val breedResponse = MutableLiveData<List<String>>()

    suspend fun fetchBreedList() {
        breedResponse.value = getAllBreed()
    }

    private suspend fun getAllBreed() : List<String>
            = withContext(Dispatchers.IO) {
        var breedList: BreedResponse? = null
        try {
            breedList = DogWalkerServiceApi.DogWalkerService.getAllBreed()!!.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
        }

        val listBreed = arrayListOf<String>()
        for (list: Breed in breedList!!.body) {
            listBreed.add(list.name)
        }

        listBreed
    }

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

    suspend fun updateDog(session: String, dog: Dog, file: MultipartBody.Part?, breedId: Int?) {
        updateDogResponse.value = updateDogBackground(session, dog, file, breedId)
    }

    private suspend fun updateDogBackground(session: String, dog: Dog, file: MultipartBody.Part?, breedId: Int?) : CommonResponse?
            = withContext(Dispatchers.IO) {
        var resp: CommonResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.updateDogInformation(
                session = session,
                age = dog.age,
                breedId = breedId,
                gender = dog.gender,
                name = dog.name,
                specialNeeds = dog.specialNeeds,
                weight = dog.weight,
                Id = dog.id,
                photo = file
            )?.await()
        } catch(e: Exception) {
            Log.e(TAG, "Update dog err: ${e.message}")
            e.printStackTrace()
        }

        resp
    }

    suspend fun deleteDog(session: String, id: Int) {
        deleteDogResponse.value = deleteDogBackground(session, id)
    }

    private suspend fun deleteDogBackground(session: String, id: Int) : CommonResponse?
            = withContext(Dispatchers.IO) {
        var resp: CommonResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.deleteDog(session, DeleteDogRequest(id))?.await()
        } catch(e: Exception) {
            Log.e(TAG, "Delete dog error: ${e.message}")
            e.printStackTrace()
        }

        resp
    }

}