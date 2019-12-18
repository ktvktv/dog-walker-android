package com.example.dogwalker.data

data class BreedResponse(
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: List<Breed>
)