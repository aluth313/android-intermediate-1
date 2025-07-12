package com.aluth.storyapp.ui.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aluth.storyapp.di.Injection
import com.aluth.storyapp.data.local.datastore.SessionPreferences
import com.aluth.storyapp.data.repository.StoryRepository
import com.aluth.storyapp.ui.auth.AuthViewModel
import com.aluth.storyapp.ui.core.PreferencesViewModel
import com.aluth.storyapp.ui.story.StoryViewModel

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
    private val sessionPreferences: SessionPreferences? = null,
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            application: Application? = null,
            sessionPreferences: SessionPreferences? = null,
        ): ViewModelFactory? {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: application?.let {
                    ViewModelFactory(
                        Injection.storyRepository(),
                        sessionPreferences,
                    ).also { INSTANCE = it }
                }
            }
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        when {
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                return AuthViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(StoryViewModel::class.java) -> {
                return StoryViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(PreferencesViewModel::class.java) -> {
                if (sessionPreferences == null) {
                    throw IllegalArgumentException("SessionPreferences is required for PreferencesViewModel")
                }
                return PreferencesViewModel(sessionPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}