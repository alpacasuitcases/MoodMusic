package com.example.moodmusic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmusic.data.repository.MusicRepository
import com.example.moodmusic.domain.model.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.SocketException
import java.net.UnknownHostException

class SongsViewModel(
    private val repository: MusicRepository
) : ViewModel() {

    private val _songs = MutableStateFlow<List<Song>>(emptyList())
    val songs: StateFlow<List<Song>> = _songs

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun searchSongs(query: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            val result = repository.searchSongs(query)

            result
                .onSuccess { list ->
                    _songs.value = list
                    if (list.isEmpty()) {
                        _error.value = "No se encontraron canciones para esta emoción"
                    }
                }
                .onFailure { e ->
                    _songs.value = emptyList()
                    _error.value = when (e) {
                        is UnknownHostException -> "No hay conexión a Internet"
                        is SocketException -> "El emulador no puede conectarse a Internet"
                        else -> "Error al cargar canciones"
                    }
                }

            _loading.value = false
        }
    }
}