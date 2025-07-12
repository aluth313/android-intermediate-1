package com.aluth.storyapp.model.data

import retrofit2.http.Body
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
}