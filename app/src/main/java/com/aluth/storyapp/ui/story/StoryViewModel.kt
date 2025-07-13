package com.aluth.storyapp.ui.story

import androidx.lifecycle.ViewModel
import com.aluth.storyapp.data.repository.StoryRepository
import java.io.File

class StoryViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {
    fun getStories(token: String) = storyRepository.getStories(token)

    fun postStory(token: String, description: String, imageFile: File) =
        storyRepository.postStory(token, description, imageFile)
}