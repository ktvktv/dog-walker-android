package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.LOGIN_SUCCESSFUL
import com.example.dogwalker.data.*
import com.example.dogwalker.network.DogWalkerServiceApi

class SinglePostViewModel : ViewModel() {
    private val TAG = SinglePostViewModel::class.java.simpleName
    val postDetail = MutableLiveData<Post>()
    val commentDetail = MutableLiveData<List<Comment>>()
    val message = MutableLiveData<String>()

    suspend fun getPostDetail(session: String, postId: Int) {
        var postDetailResponse: DetailPostResponse?
        try {
            postDetailResponse = DogWalkerServiceApi.DogWalkerService.getPostDetail(postId, session)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }

        if(postDetailResponse != null) {
            if(postDetailResponse.message == LOGIN_SUCCESSFUL) {
                postDetail.value = postDetailResponse.body
            }
        }
    }

    suspend fun getCommentDetail(session: String, postId: Int) {
        var commentResponse: CommentResponse?
        try {
            commentResponse = DogWalkerServiceApi.DogWalkerService.getComment(postId, session)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            return
        }

        if(commentResponse != null) {
            if(commentResponse.message == LOGIN_SUCCESSFUL) {
                commentDetail.value = commentResponse.body
            }
        }
    }

    suspend fun insertComment(session: String, commentRequest: InsertCommentRequest) {
        var commentResponse: CommentResponse?
        try {
            commentResponse = DogWalkerServiceApi.DogWalkerService.insertComment(
                session, commentRequest)?.await()
        } catch(e: Exception) {
            Log.e(TAG, e.message)
            e.printStackTrace()
            message.value = "Unknown error, please try again"
            return
        }

        if(commentResponse != null) {
            if(commentResponse.message == LOGIN_SUCCESSFUL){
                message.value = "Success comment!"
            } else {
                message.value = commentResponse.message
            }
            return
        }

        message.value = "Unknown error, please try again"
    }
}