package com.aluth.storyapp.ui.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.aluth.storyapp.data.local.datastore.SessionPreferences
import com.aluth.storyapp.data.model.response.LoginResult
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