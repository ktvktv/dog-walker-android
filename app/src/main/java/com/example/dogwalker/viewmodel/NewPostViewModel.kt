package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.data.DetailPostResponse
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class NewPostViewModel : ViewModel() {
    private val TAG = NewPostViewModel::class.java.simpleName
    val postResponse = MutableLiveData<DetailPostResponse>()

    suspend fun getPost(session: String, postID: Int) {
        postResponse.value = findPost(session, postID)
    }

    private suspend fun findPost(session: String, postID: Int) : DetailPostResponse?
            = withContext(Dispatchers.IO) {
        var resp: DetailPostResponse? = null

        try {
            resp = DogWalkerServiceApi.DogWalkerService.getPostDetail(postID, session)?.await()
        } catch (e: Exception) {
            Log.e(TAG, "Get post detail error: ${e.message}")
            e.printStackTrace()
        }

        resp
    }
}