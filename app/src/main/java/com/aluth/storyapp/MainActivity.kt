package com.aluth.storyapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.aluth.storyapp.model.data.LoginResult
import com.aluth.storyapp.view.LoginActivity
import com.aluth.storyapp.view.StoryActivity
import com.aluth.storyapp.view.ViewModelFactory
import com.aluth.storyapp.viewmodel.PreferencesViewModel
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pref = SessionPreferences.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(application, pref)
        val preferencesViewModel = ViewModelProvider(this, factory!!)[PreferencesViewModel::class.java]

        preferencesViewModel.getUserSession().observe(this) { session ->
            val user = Gson().fromJson(session, LoginResult::class.java)
            if (!user?.token.isNullOrEmpty()) {
                startActivity(Intent(this, StoryActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }

    }
}