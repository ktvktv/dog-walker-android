package com.example.dogwalker.data

data class WalkerResponse(
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: Walker?
)