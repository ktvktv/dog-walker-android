package com.example.dogwalker.data

data class SoloDogResponse(
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: Dog?
)