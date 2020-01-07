package com.example.dogwalker.data

data class LoginRequest (
    val phoneNumber: String,
    val password: String,
    val token: String
)