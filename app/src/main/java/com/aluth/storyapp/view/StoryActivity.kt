package com.aluth.storyapp.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aluth.storyapp.R
import com.aluth.storyapp.SessionPreferences
import com.aluth.storyapp.dataStore
import com.aluth.storyapp.databinding.ActivityStoryBinding
import com.aluth.storyapp.viewmodel.PreferencesViewModel

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
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.title = getString(R.string.story)
        binding.topAppBar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))

        binding.fabAdd.setOnClickListener {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvStory.layoutManager = layoutManager
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
}