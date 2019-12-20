package com.example.dogwalker.data

import com.squareup.moshi.Json

data class RegisterWalkerRequest(
    val description: String,
    @Json(name="maxDogSize") val maxWeight: Int,
    val pricing: Int,
    @Json(name="travelDistance") val maxDistance: Int,
    @Json(name="walkDuration") val maxDuration: Int
)