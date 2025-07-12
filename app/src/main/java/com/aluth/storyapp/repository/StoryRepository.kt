package com.aluth.storyapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aluth.storyapp.model.data.ApiService
import com.aluth.storyapp.model.data.LoginRequest
import com.aluth.storyapp.model.data.LoginResult
import com.aluth.storyapp.model.data.RegisterRequest
import com.aluth.storyapp.model.data.RegisterResponse
import com.aluth.storyapp.model.data.Result
import org.json.JSONObject
import retrofit2.HttpException

class StoryRepository private constructor (
    private val apiService: ApiService,
){
    fun login(loginRequest: LoginRequest): LiveData<Result<LoginResult>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.login(loginRequest)
            emit(Result.Success(response.loginResult!!))
        } catch (e: Exception) {
            Log.e(TAG, "login: ${e.message.toString()}")
            emit(Result.Error(e.message.toString()))
        }
    }

    fun register(registerRequest: RegisterRequest): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(registerRequest)
            if (response.error){
                emit(Result.Error(response.message))
            }
            emit(Result.Success(response))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorMessage = try {
                    val jsonObject = JSONObject(errorBody ?: "")
                    jsonObject.getString("message")
                } catch (jsonEx: Exception) {
                    "Terjadi kesalahan"
                }
                Log.e(TAG, "register msg: $errorMessage")
                emit(Result.Error(errorMessage))

            } else {
                Log.e(TAG, "register: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    companion object {
        private const val TAG = "StoryRepository"

        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}