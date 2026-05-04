package com.example.moodmusic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmusic.data.local.entity.PlaylistEntity
import com.example.moodmusic.data.repository.MusicRepository
import com.example.moodmusic.data.repository.PlaylistRepository
import com.example.moodmusic.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.moodmusic.domain.model.MoodType

class SongsViewModel(
    private val repository: MusicRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    // Playlist en memoria (para el player)
    private val _currentPlaylist = MutableStateFlow<List<Song>>(emptyList())
    val currentPlaylist: StateFlow<List<Song>> = _currentPlaylist

    // Mood actual (para guardar correctamente)
    private var currentMood: MoodType = MoodType.HAPPY
    private var currentEmotionName: String = ""

    // BUSCAR canciones
    fun searchSongs(query: String, mood: MoodType, emotionName: String) {
        viewModelScope.launch {

            currentMood = mood
            currentEmotionName = emotionName

            val result = repository.searchSongs(query)

            result.onSuccess { list ->
                _songs.value = list

                //Actualiza playlist para el player
                _currentPlaylist.value = list
            }
        }
    }

    //Guardar playlist
    fun saveCurrentPlaylist(name: String) {

        val currentSongs = _songs.value
        if (currentSongs.isEmpty()) return

        viewModelScope.launch {

            val playlistId = System.currentTimeMillis().toString()

            val entity = PlaylistEntity(
                id = playlistId,
                name = name,
                mood = currentMood.name,
                emotionName = currentEmotionName
            )

            playlistRepository.savePlaylist(entity, currentSongs)
        }
    }
    fun setPlaylist(list: List<Song>) {
        _currentPlaylist.value = list
    }
}