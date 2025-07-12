package com.aluth.storyapp

import com.aluth.storyapp.network.ApiConfig
import com.aluth.storyapp.repository.StoryRepository

object Injection {
    fun storyRepository(): StoryRepository {
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService)
    }
}