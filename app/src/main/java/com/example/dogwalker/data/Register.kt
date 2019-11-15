package com.example.dogwalker.data

import com.squareup.moshi.Json

data class Register (
    val phoneNumber: String,
    val password: String,
    val name: String,
    val address: String,
    val gender: String,
    val dateOfBirth: String,
    val placeOfBirth: String
)