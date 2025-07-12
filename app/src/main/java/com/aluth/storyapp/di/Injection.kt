package com.aluth.storyapp.di

import com.aluth.storyapp.data.network.ApiConfig
import com.aluth.storyapp.data.repository.StoryRepository

object Injection {
    fun storyRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService)
    }
}