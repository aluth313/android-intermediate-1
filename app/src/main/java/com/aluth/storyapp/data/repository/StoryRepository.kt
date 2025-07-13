package com.aluth.storyapp.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.aluth.storyapp.data.network.ApiService
import com.aluth.storyapp.data.model.request.LoginRequest
import com.aluth.storyapp.data.model.response.LoginResult
import com.aluth.storyapp.data.model.request.RegisterRequest
import com.aluth.storyapp.data.model.response.BaseResponse
import com.aluth.storyapp.utils.Result
import com.aluth.storyapp.data.model.response.Story
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: ApiService,
) {
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

    fun register(registerRequest: RegisterRequest): LiveData<Result<BaseResponse>> = liveData {
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

    fun postStory(
        token: String,
        description: String,
        imageFile: File
    ): LiveData<Result<BaseResponse>> = liveData {
        emit(Result.Loading)
        try {
            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            val response = apiService.postStory("Bearer $token", multipartBody, requestBody)
            emit(Result.Success(response))
        } catch (e: Exception) {
            if (e is HttpException) {
                val errorMessage = errMsg(e)
                Log.e(TAG, "postStory: $errorMessage")
                emit(Result.Error(errorMessage))
            } else {
                Log.e(TAG, "postStory: ${e.message.toString()}")
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

    private fun errMsg(e: HttpException): String {
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