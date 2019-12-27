package com.example.dogwalker.data

data class NotifyData(
    val transactionId: Int,
    val userImageUrl: String?,
    val description: String,
    val date: String,
    val message: String,
    val title: String
)