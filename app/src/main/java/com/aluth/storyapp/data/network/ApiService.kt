package com.aluth.storyapp.data.network

import com.aluth.storyapp.data.model.request.LoginRequest
import com.aluth.storyapp.data.model.response.LoginResponse
import com.aluth.storyapp.data.model.request.RegisterRequest
import com.aluth.storyapp.data.model.response.RegisterResponse
import com.aluth.storyapp.data.model.response.StoryListResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @GET("stories")
    suspend fun stories(
        @Header("Authorization") token: String,
    ): StoryListResponse
}