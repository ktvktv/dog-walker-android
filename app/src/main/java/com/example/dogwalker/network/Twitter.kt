package com.example.dogwalker.network

import com.example.dogwalker.data.Post
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

val BASE_URL = "https://jsonplaceholder.typicode.com/"

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

interface Twitter {
    @GET("posts")
    fun getPost(): Deferred<List<Post>>?

    @POST("posts")
    fun setPost(@Body post: Post): Deferred<Post>?
}

object TwitterServiceApi {
    val twitterService by lazy {
        retrofit.create(Twitter::class.java)
    }
}