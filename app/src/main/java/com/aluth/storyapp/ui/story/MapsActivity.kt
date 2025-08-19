package com.aluth.storyapp.ui.story

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.aluth.storyapp.R
import com.aluth.storyapp.data.local.datastore.SessionPreferences
import com.aluth.storyapp.data.local.datastore.dataStore
import com.aluth.storyapp.data.model.response.LoginResult
import com.aluth.storyapp.data.model.response.Story

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.aluth.storyapp.databinding.ActivityMapsBinding
import com.aluth.storyapp.ui.core.PreferencesViewModel
import com.aluth.storyapp.ui.factory.ViewModelFactory
import com.aluth.storyapp.utils.Result
import com.google.android.gms.maps.model.LatLngBounds
import com.google.gson.Gson

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val boundsBuilder = LatLngBounds.Builder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        val pref = SessionPreferences.getInstance(application.dataStore)
        val factory = ViewModelFactory.getInstance(application, pref)
        val preferencesViewModel = factory?.let { ViewModelProvider(this, it)[PreferencesViewModel::class.java] }
        val storyViewModel = factory?.let { ViewModelProvider(this, it)[StoryViewModel::class.java]}

        preferencesViewModel?.getUserSession()?.observe(this) { session ->
            val user = Gson().fromJson(session, LoginResult::class.java)
            if (!user?.token.isNullOrEmpty()) {
                storyViewModel?.getStoriesWithLocation(user.token!!)?.observe(this) { result ->
                    if (result is Result.Loading) {
                        Toast.makeText(this,
                            getString(R.string.memuat_beberapa_lokasi), Toast.LENGTH_SHORT).show()
                    }

                    if (result is Result.Error) {
                        Toast.makeText(this,
                            getString(R.string.gagal_memuat_lokasi), Toast.LENGTH_SHORT).show()
                    }

                    if (result is Result.Success) {
                        Toast.makeText(this,
                            getString(R.string.berhasil_memuat_lokasi), Toast.LENGTH_SHORT).show()
                        addAnyMarker(result.data, mMap)
                    }
                }
            }
        }
    }

    private fun addAnyMarker(data: List<Story>, mMap: GoogleMap) {
        data.forEach { story ->
            val latLng = LatLng(story.lat, story.lon)
            mMap.addMarker(MarkerOptions().position(latLng).title(story.name))
            boundsBuilder.include(latLng)
        }

        val bounds: LatLngBounds = boundsBuilder.build()
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngBounds(
                bounds,
                resources.displayMetrics.widthPixels,
                resources.displayMetrics.heightPixels,
                300
            )
        )

    }

}