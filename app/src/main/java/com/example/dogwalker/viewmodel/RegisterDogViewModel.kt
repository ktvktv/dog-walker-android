package com.example.dogwalker.viewmodel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.adapter.BreedAdapter
import com.example.dogwalker.data.Breed
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class RegisterDogViewModel(val context: Context, var breedAdapter: BreedAdapter) : ViewModel() {

    private val TAG = RegisterDogViewModel::class.java.simpleName

    private val listBreed = MutableLiveData<List<Breed>>()

    private val job = Job()

    private val coroutineScope = CoroutineScope(job + Dispatchers.Main)

    init {
        Log.d(TAG, "Init view model")
        coroutineScope.launch {
            fetchBreedList()
        }
    }

    private suspend fun fetchBreedList() {
        Log.d(TAG, "Fetching breed data.")
        try {
            listBreed.value = DogWalkerServiceApi.DogWalkerService.getAllBreed()!!.await().result
        } catch(e: Exception) {
            Toast.makeText(context, "Error message when get breed data", Toast.LENGTH_SHORT).show()
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }

        Log.d(TAG, "listBreed size: ${listBreed.value!!.size}")

        val breedValue = ArrayList<String>()

        for(i in listBreed.value!!) {
            breedValue.add(i.name)
        }

        breedAdapter.addAll(breedValue)
    }

    fun getBreedList(): List<Breed>? {
        return listBreed.value
    }
}