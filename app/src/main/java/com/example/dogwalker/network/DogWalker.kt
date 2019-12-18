package com.example.dogwalker.network

import com.example.dogwalker.data.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.RequestBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

val BASE_URL = "http://35.240.229.20/"

val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

val retrofit = Retrofit.Builder()
    .baseUrl(BASE_URL)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .build()

interface DogWalker {
    @POST("user/register")
    fun register(@Body register: Register): Deferred<LoginResponse>?

    @POST("user/login")
    fun login(@Body login: LoginRequest): Deferred<LoginResponse>?

    @Multipart
    @POST("dog/register")
    fun registerDog(@Part("owner_id") ownerId: Int,
                    @Part("breed_id") breedId: Int,
                    @Part("age") age: Int,
                    @Part("weight") weight: Int,
                    @Part("gender") gender: String,
                    @Part("name") name: String,
                    @Part("special_needs") specialNeeds: String,
                    @Part("photo") photo: RequestBody): Deferred<CommonResponse>?

    @GET("breed/")
    fun getAllBreed() : Deferred<BreedResponse>?
}

object DogWalkerServiceApi {
    val DogWalkerService by lazy {
        retrofit.create(DogWalker::class.java)
    }
}