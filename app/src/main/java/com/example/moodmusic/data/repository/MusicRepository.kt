package com.example.moodmusic.data.repository

import android.util.Log
import com.example.moodmusic.data.remote.api.ItunesApiService
import com.example.moodmusic.data.remote.mapper.toDomain
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.domain.model.Song

class MusicRepository(
    private val api: ItunesApiService
) {
    suspend fun getSongsByMood(mood: MoodType): Result<List<Song>> {
        return try {
            val response = api.searchSongs(term = mood.query)

            Log.d("MoodMusic", "Mood: ${mood.name} | resultCount: ${response.resultCount}")

            val songs = response.results
                .map { it.toDomain(mood.name) }
                .filter {
                    it.id.isNotBlank() &&
                            it.title.isNotBlank() &&
                            it.artist.isNotBlank()
                }
                .distinctBy { it.id }

            Result.success(songs)
        } catch (e: Exception) {
            Log.e("MoodMusic", "Error cargando canciones", e)
            Result.failure(e)
        }
    }
}