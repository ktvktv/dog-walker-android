package com.example.dogwalker.data

import com.squareup.moshi.Json

data class CommonResponse (
    val message: String,
    @Json(name="status_code") val statusCode: Int
)