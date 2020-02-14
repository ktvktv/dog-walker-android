package com.example.dogwalker.data

data class MapsDetail(
    val id: Int,
    val status: String,
    val walkDate: String,
    val price: Int,
    val duration: Int,
    val clientId: Int,
    val phoneNumber: String,
    val isRated: Boolean,
    val name: String,
    val address: String,
    val photo: String?,
    val dogId: Int
)