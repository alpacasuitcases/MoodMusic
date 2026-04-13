package com.example.moodmusic.data.repository

import com.example.moodmusic.data.remote.api.ItunesApiService
import com.example.moodmusic.data.remote.mapper.toDomain
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.domain.model.Song

class MusicRepository(
    private val api: ItunesApiService
) {
    suspend fun getSongsByMood(mood: MoodType): List<Song> {
        val query = mapMoodToQuery(mood)
        val response = api.searchSongs(term = query, limit = 20)

        return response.results
            .mapNotNull { dto ->
                val id = dto.trackId?.toString()
                if (id.isNullOrEmpty()) null else dto.toDomain(mood.name)
            }
            .distinctBy { it.id }
    }

    private fun mapMoodToQuery(mood: MoodType): String {
        return when (mood) {
            MoodType.HAPPY -> "happy upbeat pop"
            MoodType.SAD -> "sad acoustic emotional"
            MoodType.RELAXED -> "relax calm chill"
            MoodType.FOCUSED -> "focus study instrumental"
            MoodType.ENERGETIC -> "workout dance energy"
            MoodType.ROMANTIC -> "romantic love songs"
        }
    }
}