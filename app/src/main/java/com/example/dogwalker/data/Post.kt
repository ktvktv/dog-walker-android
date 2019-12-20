package com.example.dogwalker.data

import com.squareup.moshi.Json

data class Post(
    val id: Int,
    val title: String,
    val content: String,
    @Json(name="createdAt") val date: String,
    val name: String,
    val photo: String?
)