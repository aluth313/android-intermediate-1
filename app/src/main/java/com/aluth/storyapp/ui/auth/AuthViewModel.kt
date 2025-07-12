package com.aluth.storyapp.ui.auth

import androidx.lifecycle.ViewModel
import com.aluth.storyapp.data.model.request.LoginRequest
import com.aluth.storyapp.data.model.request.RegisterRequest
import com.aluth.storyapp.data.repository.StoryRepository

class AuthViewModel(
    private val storyRepository: StoryRepository,
) : ViewModel() {
    fun register(registerRequest: RegisterRequest) = storyRepository.register(registerRequest)
    fun login(loginRequest: LoginRequest) = storyRepository.login(loginRequest)
}