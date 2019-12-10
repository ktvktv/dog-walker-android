package com.example.dogwalker.data

data class Breed(
    val id: String,
    val name: String
)

data class BreedResponse(
    val result: List<Breed>
)