package com.example.dogwalker.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dogwalker.SUCCESSFUL
import com.example.dogwalker.data.*
import com.example.dogwalker.network.DogWalkerServiceApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SinglePostViewModel : ViewModel() {
    private val TAG = SinglePostViewModel::class.java.simpleName
    val postDetail = MutableLiveData<Post>()
    val commentDetail = MutableLiveData<List<Comment>>()
    val message = MutableLiveData<String>()
    val updatePostResp = MutableLiveData<CommonResponse>()
    val deletePostResp = MutableLiveData<CommonResponse>()

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
            if(postDetailResponse.message == SUCCESSFUL) {
                postDetail.value = postDetailResponse.body
            }
        }
    }

    suspend fun updatePost(session: String, post: InsertPostRequest, id: Int) {
        updatePostResp.value = updatePostBackground(session, post, id)
    }

    private suspend fun updatePostBackground(session: String, post: InsertPostRequest, id: Int) : CommonResponse?
            = withContext(Dispatchers.IO) {
        var resp: CommonResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.updatePost(id, session, post)?.await()
        } catch(e: Exception) {
            Log.e(TAG, "Update post error: ${e.message}")
            e.printStackTrace()
        }

        resp
    }

    suspend fun deletePost(session: String, id: Int) {
        deletePostResp.value = deletePostBackground(session, id)
    }

    private suspend fun deletePostBackground(session: String, id: Int) : CommonResponse?
            = withContext(Dispatchers.IO) {
        var resp: CommonResponse? = null
        try {
            resp = DogWalkerServiceApi.DogWalkerService.deletePost(id, session)?.await()
        } catch(e: Exception) {
            Log.e(TAG, "Delete post error: ${e.message}")
            e.printStackTrace()
        }

        resp
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
            if(commentResponse.message == SUCCESSFUL) {
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
            if(commentResponse.message == SUCCESSFUL){
                message.value = "Success comment!"
            } else {
                message.value = commentResponse.message
            }
            return
        }

        message.value = "Unknown error, please try again"
    }
}