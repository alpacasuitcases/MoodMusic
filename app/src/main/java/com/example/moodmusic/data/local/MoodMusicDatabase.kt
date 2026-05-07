package com.example.moodmusic.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.moodmusic.data.local.dao.FavoriteSongDao
import com.example.moodmusic.data.local.dao.MoodHistoryDao
import com.example.moodmusic.data.local.dao.PlaylistDao
import com.example.moodmusic.data.local.entity.FavoriteSongEntity
import com.example.moodmusic.data.local.entity.MoodHistoryEntity
import com.example.moodmusic.data.local.entity.PlaylistEntity
import com.example.moodmusic.data.local.entity.PlaylistSongEntity

@Database(
    entities = [FavoriteSongEntity::class,
                MoodHistoryEntity::class,
                PlaylistEntity::class,
                PlaylistSongEntity::class,
               ],
    version = 4,
    exportSchema = false
)
abstract class MoodMusicDatabase : RoomDatabase() {

    abstract fun favoriteSongDao(): FavoriteSongDao
    abstract fun moodHistoryDao(): MoodHistoryDao

    abstract fun playlistDao(): PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: MoodMusicDatabase? = null

        fun getDatabase(context: Context): MoodMusicDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoodMusicDatabase::class.java,
                    "moodmusic_db"
                )
                    .fallbackToDestructiveMigration(true)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}