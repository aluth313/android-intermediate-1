package com.aluth.storyapp.data.network

import com.aluth.storyapp.data.model.request.LoginRequest
import com.aluth.storyapp.data.model.response.LoginResponse
import com.aluth.storyapp.data.model.request.RegisterRequest
import com.aluth.storyapp.data.model.response.BaseResponse
import com.aluth.storyapp.data.model.response.Story
import com.aluth.storyapp.data.model.response.StoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): BaseResponse

    @GET("stories")
    suspend fun stories(
        @Header("Authorization") token: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoryListResponse

    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): BaseResponse
}