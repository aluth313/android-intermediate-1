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
import com.aluth.storyapp.model.data.Story
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
            if (e is HttpException) {
                val errorMessage = errMsg(e)
                Log.e(TAG, "login: $errorMessage")
                emit(Result.Error(errorMessage))
            } else {
                Log.e(TAG, "login: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun register(registerRequest: RegisterRequest): LiveData<Result<RegisterResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.register(registerRequest)
            emit(Result.Success(response))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorMessage = errMsg(e)
                Log.e(TAG, "register: $errorMessage")
                emit(Result.Error(errorMessage))
            } else {
                Log.e(TAG, "register: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun getStories(token: String): LiveData<Result<List<Story>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.stories("Bearer $token")
            emit(Result.Success(response.listStory))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorMessage = errMsg(e)
                Log.e(TAG, "getStories: $errorMessage")
                emit(Result.Error(errorMessage))
            } else {
                Log.e(TAG, "getStories: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    private fun errMsg(e: HttpException) : String {
        val errorBody = e.response()?.errorBody()?.string()
        val errorMessage = try {
            val jsonObject = JSONObject(errorBody ?: "")
            jsonObject.getString("message")
        } catch (jsonEx: Exception) {
            "Terjadi kesalahan"
        }
        return errorMessage
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