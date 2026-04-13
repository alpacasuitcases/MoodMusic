package com.example.moodmusic.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moodmusic.data.local.dao.FavoriteSongDao
import com.example.moodmusic.data.local.dao.MoodHistoryDao
import com.example.moodmusic.data.local.entity.FavoriteSongEntity
import com.example.moodmusic.data.local.entity.MoodHistoryEntity

@Database(
    entities = [FavoriteSongEntity::class, MoodHistoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MoodMusicDatabase : RoomDatabase() {

    abstract fun favoriteSongDao(): FavoriteSongDao
    abstract fun moodHistoryDao(): MoodHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: MoodMusicDatabase? = null

        fun getDatabase(context: Context): MoodMusicDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodMusicDatabase::class.java,
                    "moodmusic_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}