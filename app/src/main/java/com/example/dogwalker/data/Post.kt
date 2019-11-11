package com.example.dogwalker.data

import com.squareup.moshi.Json

data class Post (
    @Json(name= "userId") val UserID: Int,
    @Json(name= "id") val ID: Int,
    @Json(name= "title") val Title: String,
    @Json(name= "body") val Body: String
)