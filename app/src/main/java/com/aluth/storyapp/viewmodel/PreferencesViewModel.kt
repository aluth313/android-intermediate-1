package com.aluth.storyapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aluth.storyapp.SessionPreferences
import com.aluth.storyapp.model.data.LoginResult
import kotlinx.coroutines.launch

class PreferencesViewModel(
    private val sessionPreferences: SessionPreferences,
) : ViewModel() {
    fun getUserSession(): LiveData<String>{
        return sessionPreferences.getUserSession().asLiveData()
    }

    fun saveUserSession(loginResult: LoginResult) {
        viewModelScope.launch {
            sessionPreferences.saveUserSession(loginResult)
        }
    }

    fun clearUserSession() {
        viewModelScope.launch {
            sessionPreferences.clearUserSession()
        }
    }
}