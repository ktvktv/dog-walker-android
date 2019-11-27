package com.example.dogwalker.data

import okhttp3.RequestBody
import retrofit2.http.Part

data class Dog(
    @Part("breed_id") val breedId: RequestBody,
    val age: RequestBody,
    val weight: RequestBody,
    @Part("special_needs") val specialNeeds: RequestBody,
    val photo: RequestBody
)