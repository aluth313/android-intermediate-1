package com.aluth.storyapp.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.aluth.storyapp.R
import com.aluth.storyapp.databinding.ActivityRegisterBinding
import com.aluth.storyapp.model.data.RegisterRequest
import com.aluth.storyapp.model.data.Result
import com.aluth.storyapp.viewmodel.AuthViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val factory = ViewModelFactory.getInstance(application)
        val authViewModel = ViewModelProvider(this, factory!!)[AuthViewModel::class.java]
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = getString(R.string.register)
        binding.topAppBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.topAppBar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.white))

        binding.btnRegister.setOnClickListener {
            authViewModel.register(
                RegisterRequest(
                    binding.edRegisterName.text.toString(),
                    binding.edRegisterEmail.text.toString(),
                    binding.edRegisterPassword.text.toString(),
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
                        binding.pbLoading.visibility = View.GONE
                        Toast.makeText(
                            this,
                            result.data.message,
                            Toast.LENGTH_SHORT,
                        ).show()
                        val intent = Intent(this, StoryActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}