package com.aluth.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.aluth.storyapp.repository.StoryRepository

class StoryViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {
    fun getStories(token: String) = storyRepository.getStories(token)
}