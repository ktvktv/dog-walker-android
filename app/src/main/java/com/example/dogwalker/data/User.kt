package com.example.dogwalker.data

data class User(
    val name: String,
    val password: String,
    val email: String,
    val phoneNumber: String,
    val gender: String,
    val address: String,
    val birthDate: String,
    val birthPlace: String,
    val userImageUrl: String,
    val dog: List<Dog>
)