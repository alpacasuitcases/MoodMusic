package com.example.moodmusic.di

import android.content.Context
import com.example.moodmusic.data.local.MoodMusicDatabase
import com.example.moodmusic.data.remote.api.RetrofitInstance
import com.example.moodmusic.data.repository.FavoritesRepository
import com.example.moodmusic.data.repository.MoodHistoryRepository
import com.example.moodmusic.data.repository.MusicRepository
import com.example.moodmusic.data.repository.PlaylistRepository
import com.example.moodmusic.domain.model.Song

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

    val playlistRepository: PlaylistRepository by lazy {
        PlaylistRepository(database.playlistDao())
    }

    fun init(context: Context) {
        appContext = context.applicationContext
    }

    var playerQueue: List<Song> = emptyList()
}