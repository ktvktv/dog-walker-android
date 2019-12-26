package com.example.dogwalker.data

data class PostOrderRequest(
    val dogId: Int,
    val duration: Int,
    val walkDate: String,
    val walkerId: Int
)