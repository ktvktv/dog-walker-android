package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.InsertPostRequest
import com.example.dogwalker.data.Post
import com.example.dogwalker.data.PostResponse
import com.example.dogwalker.network.DogWalkerServiceApi
import java.lang.Exception

class PostViewModel : ViewModel() {
    private val TAG = PostViewModel::class.java.simpleName
    val postList = MutableLiveData<List<Post>>()
    val isInsertPostSuccess = MutableLiveData<Boolean>()
    private val postMessage = MutableLiveData<String>()

    suspend fun InsertPost(session: String, postRequest: InsertPostRequest) {
        var postResponse: CommonResponse?
        try {
            postResponse = DogWalkerServiceApi.DogWalkerService.insertPost(session,
                postRequest)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            isInsertPostSuccess.value = false
            return
        }

        if(postResponse != null) {
            if(postResponse.message == SUCCESSFUL) {
                isInsertPostSuccess.value = true
                return
            } else {
                postMessage.value = postResponse.message
                isInsertPostSuccess.value = false
                return
            }
        }

        postMessage.value = "Unknown error, please try again"
        isInsertPostSuccess.value = false
    }

    suspend fun GetAllPost(session: String) {
        val postResponse: PostResponse?
        try {
            postResponse = DogWalkerServiceApi.DogWalkerService.getPost(session)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }

        if(postResponse != null) {
            if(postResponse.message == SUCCESSFUL) {
                postList.value = postResponse.body
                return
            }

            postMessage.value = postResponse.message
        }

        postMessage.value = "Unknown error, please try again"
    }

    fun getPostMessage() : String? {
        return postMessage.value
    }
}