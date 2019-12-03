package com.example.dogwalker.data

import okhttp3.RequestBody
import retrofit2.http.Part

data class Dog(
    @Part("owner_id") val ownerId: Int,
    @Part("breed_id") val breedId: Int,
    @Part("age") val age: Int,
    @Part("weight") val weight: Int,
    @Part("gender") val gender: String,
    @Part("name") val name: String,
    @Part("special_needs") val specialNeeds: String,
    @Part("photo") val photo: RequestBody?
)