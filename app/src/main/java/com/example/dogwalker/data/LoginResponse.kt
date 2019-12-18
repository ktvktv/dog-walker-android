package com.example.dogwalker.data

data class LoginResponse(
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val session: String?,
    val body: User?
)