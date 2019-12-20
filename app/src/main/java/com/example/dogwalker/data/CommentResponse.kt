package com.example.dogwalker.data

data class CommentResponse(
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: List<Comment>?
)