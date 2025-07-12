package com.aluth.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aluth.storyapp.R
import com.aluth.storyapp.databinding.ActivityAddStoryBinding

class AddStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = getString(R.string.new_story)
        binding.topAppBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.topAppBar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.white))

        binding.buttonAdd.setOnClickListener {
            val intent = Intent(this, StoryActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}