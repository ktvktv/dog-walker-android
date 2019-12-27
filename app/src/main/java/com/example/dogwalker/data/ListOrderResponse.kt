package com.example.dogwalker.data

data class ListOrderResponse(
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: List<Order>?
)