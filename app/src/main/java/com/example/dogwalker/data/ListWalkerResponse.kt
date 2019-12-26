package com.example.dogwalker.data

data class ListWalkerResponse (
    val message: String?,
    val error: String?,
    val statusCode: Int,
    val body: List<Walker>?
)