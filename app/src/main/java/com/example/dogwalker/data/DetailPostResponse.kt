package com.example.dogwalker.data

data class DetailPostResponse (
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: Post?
)