package com.aluth.storyapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.aluth.storyapp.R
import com.aluth.storyapp.SessionPreferences
import com.aluth.storyapp.dataStore
import com.aluth.storyapp.databinding.ActivityLoginBinding
import com.aluth.storyapp.model.data.LoginRequest
import com.aluth.storyapp.model.data.Result
import com.aluth.storyapp.viewmodel.AuthViewModel
import com.aluth.storyapp.viewmodel.PreferencesViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val pref = SessionPreferences.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(application, pref)
        val authViewModel = ViewModelProvider(this, factory!!)[AuthViewModel::class.java]
        val preferencesViewModel = ViewModelProvider(this, factory)[PreferencesViewModel::class.java]

        binding.btnLogin.setOnClickListener {
            authViewModel.login(
                LoginRequest(
                    binding.edLoginEmail.text.toString(),
                    binding.edLoginPassword.text.toString(),
                )
            ).observe(this) { result ->
                when (result) {
                    is Result.Loading -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }

                    is Result.Error -> {
                        binding.pbLoading.visibility = View.GONE
                        Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                    }

                    is Result.Success -> {
                        preferencesViewModel.saveUserSession(result.data)
                        binding.pbLoading.visibility = View.GONE
                        Toast.makeText(
                            this,
                            getString(R.string.berhasil_login),
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent = Intent(this, StoryActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        binding.tvSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}