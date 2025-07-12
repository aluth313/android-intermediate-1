package com.aluth.storyapp.view

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aluth.storyapp.R
import com.aluth.storyapp.databinding.ActivityDetailStoryBinding
import com.aluth.storyapp.model.data.Story
import com.aluth.storyapp.utils.AppConst
import com.bumptech.glide.Glide
import com.google.gson.Gson

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = getString(R.string.detail_story)
        binding.topAppBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.topAppBar.navigationIcon?.setTint(ContextCompat.getColor(this, R.color.white))

        val storyBundle = intent.getStringExtra(AppConst.STORY)
        val story = Gson().fromJson(storyBundle, Story::class.java)

        Glide.with(this)
            .load(story.photoUrl)
            .into(binding.ivItemPhoto)
        binding.tvItemName.text = story.name
        binding.tvItemDesc.text = story.description
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }
}