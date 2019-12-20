package com.example.dogwalker.data

data class PostResponse (
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: List<Post>?
)