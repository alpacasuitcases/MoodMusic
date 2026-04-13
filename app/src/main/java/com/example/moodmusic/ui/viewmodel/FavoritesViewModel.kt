package com.example.moodmusic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmusic.data.repository.FavoritesRepository
import com.example.moodmusic.domain.model.Song
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(
    private val repository: FavoritesRepository
) : ViewModel() {

    val favorites = repository.getAllFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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
}