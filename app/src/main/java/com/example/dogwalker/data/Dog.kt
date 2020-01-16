package com.example.dogwalker.data

class Dog (
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val photo: String?,
    val weight: Int? = null,
    val specialNeeds: String? = null,
    val breedName: String = "",
    val breedId: Int? = null
)