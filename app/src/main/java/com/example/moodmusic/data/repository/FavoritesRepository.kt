package com.example.moodmusic.data.repository

import com.example.moodmusic.data.local.toDomain
import com.example.moodmusic.data.local.toEntity
import com.example.moodmusic.data.local.dao.FavoriteSongDao
import com.example.moodmusic.domain.model.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoritesRepository(
    private val dao: FavoriteSongDao
) {

    fun getAllFavorites(): Flow<List<Song>> {
        return dao.getAllFavorites().map { list ->
            list.map { it.toDomain() }
        }
    }

    suspend fun addFavorite(song: Song) {
        dao.insertFavorite(song.toEntity())
    }

    suspend fun removeFavorite(songId: String) {
        dao.deleteFavorite(songId)
    }
}