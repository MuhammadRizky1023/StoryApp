package com.example.storyapp.API

import com.example.storyapp.Model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface StoryApiService {
    @Headers("Content-Type: application/json")
    @POST("register")
    fun createUserStories(@Body requestRegister: RequestRegister): Call<ResponseMessage>

    @POST("login")
    fun loginUserStories(@Body requestLogin: LoginRequest): Call<LoginResponse>

    @GET("stories")
    fun getStoriesUser(
        @Header("Authorization") token: String
    ): Call<ResponseStory>

    @Multipart
    @POST("stories")
    fun uploadImageStories(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): Call<ResponseMessage>

}