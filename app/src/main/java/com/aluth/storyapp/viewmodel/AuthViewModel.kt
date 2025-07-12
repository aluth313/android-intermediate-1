package com.aluth.storyapp.viewmodel

import androidx.lifecycle.ViewModel
import com.aluth.storyapp.model.data.LoginRequest
import com.aluth.storyapp.model.data.RegisterRequest
import com.aluth.storyapp.repository.StoryRepository

class AuthViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {
    fun register(registerRequest: RegisterRequest) = storyRepository.register(registerRequest)
    fun login(loginRequest: LoginRequest) = storyRepository.login(loginRequest)
}