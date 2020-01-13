package com.example.dogwalker.data

data class RateResponse(
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: Order
)