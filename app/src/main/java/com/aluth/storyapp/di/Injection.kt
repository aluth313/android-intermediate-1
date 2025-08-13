package com.aluth.storyapp.di

import android.content.Context
import com.aluth.storyapp.data.local.database.StoryDatabase
import com.aluth.storyapp.data.network.ApiConfig
import com.aluth.storyapp.data.repository.StoryRepository

object Injection {
    fun storyRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(database, apiService)
    }
}