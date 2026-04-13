package com.example.moodmusic.di

import android.content.Context
import com.example.moodmusic.data.local.MoodMusicDatabase
import com.example.moodmusic.data.remote.api.RetrofitInstance
import com.example.moodmusic.data.repository.FavoritesRepository
import com.example.moodmusic.data.repository.MoodHistoryRepository
import com.example.moodmusic.data.repository.MusicRepository

object AppContainer {

    private lateinit var appContext: Context

    private val database: MoodMusicDatabase by lazy {
        MoodMusicDatabase.getDatabase(appContext)
    }

    val musicRepository: MusicRepository by lazy {
        MusicRepository(RetrofitInstance.api)
    }

    val favoritesRepository: FavoritesRepository by lazy {
        FavoritesRepository(database.favoriteSongDao())
    }

    val moodHistoryRepository: MoodHistoryRepository by lazy {
        MoodHistoryRepository(database.moodHistoryDao())
    }

    fun init(context: Context) {
        appContext = context.applicationContext
    }
}