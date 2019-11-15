package com.example.dogwalker.network

import com.example.dogwalker.data.CommonResponse
import com.example.dogwalker.data.Login
import com.example.dogwalker.data.Register
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

val BASE_URL = "http://35.240.229.20/"

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

interface DogWalker {
    @POST("user/register")
    fun register(@Body register: Register): Deferred<CommonResponse>?

    @POST("user/login")
    fun login(@Body login: Login): Deferred<CommonResponse>?
}

object DogWalkerServiceApi {
    val DogWalkerService by lazy {
        retrofit.create(DogWalker::class.java)
    }
}