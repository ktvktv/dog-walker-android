package com.example.dogwalker.data

data class WalkerUpdateRequest(
    val description: String,
    val maxDogSize: Int,
    val pricing: Int,
    val travelDistance: Int,
    val walkDuration: Int,
    val isVerified: Boolean,
    val isRecommended: Boolean
)