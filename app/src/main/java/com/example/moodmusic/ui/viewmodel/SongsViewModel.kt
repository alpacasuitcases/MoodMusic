package com.example.moodmusic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmusic.data.repository.MusicRepository
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SongsViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    fun loadSongsByMood(mood: MoodType) {
        viewModelScope.launch {
            _loading.value = true
            _songs.value = repository.getSongsByMood(mood)
            _loading.value = false
        }
    }
}