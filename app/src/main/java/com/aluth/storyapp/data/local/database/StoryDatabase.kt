package com.aluth.storyapp.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aluth.storyapp.data.local.dao.RemoteKeysDao
import com.aluth.storyapp.data.local.entity.RemoteKeys

@Database(
    entities = [RemoteKeys::class],
    version = 1,
    exportSchema = false,
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoryDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): StoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoryDatabase::class.java, "story_database"
                )
                    .fallbackToDestructiveMigration(false)
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}