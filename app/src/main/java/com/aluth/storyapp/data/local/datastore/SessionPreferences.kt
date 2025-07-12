package com.aluth.storyapp.data.local.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.aluth.storyapp.data.model.response.LoginResult
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "sessions")
class SessionPreferences private constructor(private val dataStore: DataStore<Preferences>) {
    private val userSession = stringPreferencesKey("user_session")

    fun getUserSession(): Flow<String> {
        return dataStore.data.map { prefs ->
            prefs[userSession] ?: ""
        }
    }

    suspend fun saveUserSession(loginResult: LoginResult) {
        dataStore.edit { prefs ->
            val userJson = Gson().toJson(loginResult)
            prefs[userSession] = userJson
        }
    }

    suspend fun clearUserSession() {
        dataStore.edit { prefs ->
            prefs.remove(userSession)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SessionPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): SessionPreferences {
            return INSTANCE ?: synchronized(this){
                val instance = SessionPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}