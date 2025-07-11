package com.aluth.storyapp.ui.story

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aluth.storyapp.R
import com.aluth.storyapp.data.local.datastore.SessionPreferences
import com.aluth.storyapp.data.local.datastore.dataStore
import com.aluth.storyapp.databinding.ActivityStoryBinding
import com.aluth.storyapp.data.model.response.LoginResult
import com.aluth.storyapp.utils.Result
import com.aluth.storyapp.data.model.response.Story
import com.aluth.storyapp.ui.auth.LoginActivity
import com.aluth.storyapp.ui.factory.ViewModelFactory
import com.aluth.storyapp.ui.core.PreferencesViewModel
import com.google.gson.Gson

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val pref = SessionPreferences.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(application, pref)
        val preferencesViewModel = ViewModelProvider(this, factory!!)[PreferencesViewModel::class.java]
        val storyViewModel = ViewModelProvider(this, factory)[StoryViewModel::class.java]

        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = getString(R.string.story)
        binding.topAppBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager

        preferencesViewModel.getUserSession().observe(this) { session ->
            val user = Gson().fromJson(session, LoginResult::class.java)
            if (!user?.token.isNullOrEmpty()) {
                storyViewModel.getStories(user.token ?: "").observe(this) { result ->
                    when (result) {
                        is Result.Loading -> {
                            binding.pbLoading.visibility = View.VISIBLE
                        }

                        is Result.Error -> {
                            binding.pbLoading.visibility = View.GONE
                            Toast.makeText(this, result.error, Toast.LENGTH_SHORT).show()
                        }

                        is Result.Success -> {
                            setEventData(result.data)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_top_app_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                val pref = SessionPreferences.getInstance(application.dataStore)
                val factory = ViewModelFactory.getInstance(application, pref)
                val preferencesViewModel = ViewModelProvider(this, factory!!)[PreferencesViewModel::class.java]
                preferencesViewModel.clearUserSession()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    private fun setEventData(stories: List<Story>) {
        binding.pbLoading.visibility = View.GONE
        val adapter = StoryAdapter()
        adapter.submitList(stories)
        binding.rvStory.adapter = adapter
        if(stories.isEmpty()){
            binding.tvEmpty.visibility = View.VISIBLE
        }
    }
}