package com.example.dogwalker.data

import com.squareup.moshi.Json

data class User(
    val id: Int,
    val email: String,
    val phoneNumber: String,
    val password: String,
    val name: String,
    val nik: String,
    val gender: String,
    val address: String,
    val isWalker: Boolean,
    val type: String,
    @Json(name = "dateOfBirth") val birthDate: String,
    @Json(name = "placeOfBirth") val birthPlace: String,
    @Json(name = "photo") val userImageUrl: String?
)