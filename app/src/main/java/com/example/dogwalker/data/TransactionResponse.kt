package com.example.dogwalker.data

data class TransactionResponse (
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: List<Transaction>?
)