package com.example.dogwalker.data

data class OrderResponse(
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: Orders
)

data class Orders(
    val id: Int
)