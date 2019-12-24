package com.example.dogwalker.data

data class TransactionRequest(
    val dogId: Int,
    val duration: Int,
    val walkDate: String
)