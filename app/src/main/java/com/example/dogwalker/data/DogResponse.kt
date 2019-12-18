package com.example.dogwalker.data

import okhttp3.RequestBody
import retrofit2.http.Part

data class DogResponse(
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: List<Dog>?
)