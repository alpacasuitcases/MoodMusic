package com.example.moodmusic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmusic.data.repository.FavoritesRepository
import com.example.moodmusic.domain.model.Song
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: FavoritesRepository
) : ViewModel() {

    val favorites: StateFlow<List<Song>> = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val favoriteIds: StateFlow<Set<String>> = repository.getAllFavorites()
        .map { list -> list.map { it.id }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    fun addFavorite(song: Song) {
        viewModelScope.launch {
            repository.addFavorite(song)
        }
    }

    fun removeFavorite(songId: String) {
        viewModelScope.launch {
            repository.removeFavorite(songId)
        }
    }

    fun toggleFavorite(song: Song, isFavorite: Boolean) {
        viewModelScope.launch {
            if (isFavorite) {
                repository.removeFavorite(song.id)
            } else {
                repository.addFavorite(song)
            }
        }
    }
}