package com.example.dogwalker.data

import com.squareup.moshi.Json

data class Comment(
    val id: Int,
    val postId: Int,
    val photo: String?,
    val name: String,
    val comment: String,
    @Json(name="createdAt") val date: String
)