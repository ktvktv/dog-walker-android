package com.example.dogwalker.data

data class Transaction(
    val id: Int,
    val isVerified: Boolean,
    val isRecommended: Boolean,
    val description: String,
    val travelDistance: Int,
    val walkDuration: Int,
    val maxDogSize: Int,
    val pricing: Int,
    val rating: Int,
    val raters: Int
)