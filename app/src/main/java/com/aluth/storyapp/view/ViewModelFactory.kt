package com.aluth.storyapp.view

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aluth.storyapp.Injection
import com.aluth.storyapp.repository.StoryRepository
import com.aluth.storyapp.viewmodel.AuthViewModel

class ViewModelFactory private constructor(
    private val storyRepository: StoryRepository,
) : ViewModelProvider.NewInstanceFactory() {

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(
            application: Application? = null,
        ): ViewModelFactory? {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: application?.let {
                    ViewModelFactory(
                        Injection.storyRepository(),
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
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}