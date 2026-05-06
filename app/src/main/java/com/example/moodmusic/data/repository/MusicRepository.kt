package com.example.moodmusic.data.repository

import android.util.Log
import com.example.moodmusic.data.remote.api.ItunesApiService
import com.example.moodmusic.data.remote.mapper.toDomain
import com.example.moodmusic.domain.model.Song

class MusicRepository(private val api: ItunesApiService) {

    // Variantes de query para cada búsqueda
    private val queryVariants = mapOf(
        "happy" to listOf("happy pop hits", "feel good music", "upbeat songs", "cheerful pop"),
        "sad" to listOf("sad songs", "emotional acoustic", "melancholy music", "heartbreak songs"),
        "energy" to listOf("energetic music", "workout beats", "pump up songs", "high energy dance"),
        "relax" to listOf("chill music", "relax lofi", "calm piano", "peaceful ambient"),
        "angry" to listOf("rock angry", "intense music", "hard rock", "metal songs"),
    )

    suspend fun searchSongs(query: String): Result<List<Song>> {
        return try {
            // Elegir variante aleatoria del query
            val baseKey = queryVariants.keys.firstOrNull {
                query.lowercase().contains(it)
            }
            val variants = queryVariants[baseKey]
            val actualQuery = if (!variants.isNullOrEmpty()) {
                variants.random()
            } else {
                // Añadir palabra aleatoria al query original para variar resultados
                val extras = listOf("", "hits", "best", "top", "new", "mix")
                "$query ${extras.random()}".trim()
            }

            Log.d("MoodMusic", "Query efectivo: $actualQuery")

            val response = api.searchSongs(term = actualQuery, limit = 50)
            val allSongs = response.results
                .map { it.toDomain(actualQuery) }
                .filter {
                    it.id.isNotBlank() &&
                            it.title.isNotBlank() &&
                            it.artist.isNotBlank()
                }
                .distinctBy { it.id }

            // Mezclar y coger los primeros 20
            val shuffled = allSongs.shuffled().take(20)
            Result.success(shuffled)
        } catch (e: Exception) {
            Log.e("MoodMusic", "Error cargando canciones", e)
            Result.failure(e)
        }
    }
}
