package com.example.dogwalker.data

import com.squareup.moshi.Json

data class CommonResponse (
    val message: String?,
    val statusCode: Int?,
    val jwt: String,
    val error: String?
)