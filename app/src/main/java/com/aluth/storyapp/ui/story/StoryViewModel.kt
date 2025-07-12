package com.aluth.storyapp.ui.story

import androidx.lifecycle.ViewModel
import com.aluth.storyapp.data.repository.StoryRepository

class StoryViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {
    fun getStories(token: String) = storyRepository.getStories(token)
}