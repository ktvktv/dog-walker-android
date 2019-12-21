package com.example.dogwalker.data

data class Walker(
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
    val dateOfBirth: String,
    val placeOfBirth: String,
    val photo: String?,
    val isVerified: Boolean,
    val isRecommended: Boolean,
    val description: String,
    val travelDistance: Int,
    val walkDuration: Int,
    val maxDogSize: Int,
    val pricing: Int,
    val rating: Int,
    val raters: Int
)