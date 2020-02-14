package com.example.dogwalker.network

import com.example.dogwalker.data.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.MultipartBody
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

    @GET("user/get/{id}")
    fun getUser(@Header("session") session: String, @Path("id") id: Int) : Deferred<CommonResponse>?

    @GET("dog/get")
    fun getDogInformation(@Header("session") session: String): Deferred<DogResponse>?

    @GET("dog/find/{id}")
    fun getDog(@Header("session") session: String, @Path("id") id: Int) : Deferred<SoloDogResponse>?

    @Multipart
    @POST("dog/update")
    fun updateDogInformation(
        @Header("session") session: String,
        @Part("age") age: Int? = null,
        @Part("breedId") breedId: Int? = null,
        @Part("gender") gender: String = "",
        @Part("name") name: String = "",
        @Part("specialNeeds") specialNeeds: String? = "",
        @Part("weight") weight: Int? = null,
        @Part("id") Id: Int,
        @Part photo: MultipartBody.Part? = null
    ): Deferred<CommonResponse>?

    @POST("dog/delete")
    fun deleteDog(
        @Header("session") session: String,
        @Body dogRequest: DeleteDogRequest
    ): Deferred<CommonResponse>?

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
        @Part("type") type: String = "",
        @Part("token") token: String = ""
    ): Deferred<CommonResponse>?

    @POST("walker/register")
    fun registerWalker(@Header("session") session: String,
                       @Body walkerData: RegisterWalkerRequest) : Deferred<CommonResponse>?

    @GET("walker/get/{id}")
    fun getWalkerData(@Header("session") session: String,
                      @Path("id") id: String) : Deferred<WalkerResponse>?

    @Multipart
    @POST("dog/register")
    fun registerDog(@Header("session") session: String,
                    @Part("breedId") breedId: Int,
                    @Part("age") age: Int,
                    @Part("weight") weight: Int,
                    @Part("gender") gender: String,
                    @Part("name") name: String,
                    @Part("specialNeeds") specialNeeds: String,
                    @Part photo: MultipartBody.Part): Deferred<CommonResponse>?

    @POST("post/upload")
    fun insertPost(@Header("session") session: String,
                   @Body postRequest: InsertPostRequest): Deferred<CommonResponse>?

    @GET("post/global")
    fun getPost(@Header("session") session: String) : Deferred<PostResponse>?

    @GET("post/find/{id}")
    fun getPostDetail(@Path("id") id: Int, @Header("session") session: String): Deferred<DetailPostResponse>?

    @POST("post/update/{id}")
    fun updatePost(@Path("id") id: Int, @Header("session") session: String, @Body post: InsertPostRequest) : Deferred<CommonResponse>?

    @GET("post/delete/{id}")
    fun deletePost(@Path("id") id: Int, @Header("session") session: String) : Deferred<CommonResponse>?

    @GET("comment/getList/{id}")
    fun getComment(@Path("id") id: Int, @Header("session") session: String): Deferred<CommentResponse>?

    @POST("comment/upload")
    fun insertComment(@Header("session") session: String, @Body commentData: InsertCommentRequest): Deferred<CommentResponse>?

    @GET("breed/")
    fun getAllBreed() : Deferred<BreedResponse>?

    @GET("transaction/find/{id}")
    fun getDetailTransaction(@Header("session") session: String,
                             @Path("id") transactionId: Int) : Deferred<MapsDetail>?

    @POST("transaction/findawalker")
    fun getFilteredWalker(@Header("session") session: String,
                          @Body listWalkerRequest: ListWalkerRequest) : Deferred<ListWalkerResponse>?

    @POST("transaction/order")
    fun postOrder(@Header("session") session: String,
                  @Body orderRequest: PostOrderRequest) : Deferred<OrderResponse>?

    @GET("transaction/get/user")
    fun getListOrderCustomer(@Header("session") session: String) : Deferred<ListOrderResponse>?

    @GET("transaction/get/walker")
    fun getWalkerListOrder(@Header("session") session: String) : Deferred<ListOrderResponse>?

    @POST("transaction/status")
    fun changeTransactionStatus(@Header("session") session: String,
                                @Body transactionStatus: TransactionStatus) : Deferred<CommonResponse>?

    @GET("transaction/reject/{id}")
    fun rejectTransaction(@Header("session") session: String,
                          @Path("id") transactionId: Int) : Deferred<CommonResponse>?

    @POST("firebase/notification")
    fun sendNotification(@Header("session") session: String,
                         @Body notificationBody: Notification) : Deferred<CommonResponse>?

    @POST("walker/rate")
    fun rateWalker(@Header("session") session: String,
                   @Body ratingRequest: RatingRequest) : Deferred<RateResponse>?

    @POST("walker/update")
    fun updateWalker(@Header("session") session: String,
                     @Body walkerUpdateRequest: WalkerUpdateRequest) : Deferred<CommonResponse>?

    @GET("transaction/isRated")
    fun getLatestTransaction(@Header("session") session: String) : Deferred<RateResponse>?
}

object DogWalkerServiceApi {
    val DogWalkerService by lazy {
        retrofit.create(DogWalker::class.java)
    }
}