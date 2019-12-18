package com.example.dogwalker.network

import com.example.dogwalker.data.*
import com.google.android.gms.common.internal.service.Common
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
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
    fun register(@Body register: Register): Deferred<CommonResponse>?

    @POST("user/login")
    fun login(@Body login: LoginRequest): Deferred<LoginResponse>?

    @GET("user/get")
    fun getUserInformation(@Header("session") session: String): Deferred<CommonResponse>?

    @GET("dog/get")
    fun getDogInformation(@Header("session") session: String): Deferred<DogResponse>?

    @Multipart
    @POST("user/update")
    fun updateUserInformation(
        @Header("session") session: String,
        @Part("address") address: String = "",
        @Part("dateOfBirth") birthdate: String = "",
        @Part("email") email: String = "",
        @Part("gender") gender: String = "",
        @Part("name") name: String = "",
        @Part("nik") nik: String = "",
        @Part("password") password: String = "",
        @Part("phoneNumber") phoneNumber: String = "",
        @Part photo: MultipartBody.Part? = null,
        @Part("placeOfBirth") birthplace: String = "",
        @Part("type") type: String = ""
    ): Deferred<CommonResponse>?

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