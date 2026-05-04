package com.example.moodmusic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmusic.data.repository.PlaylistRepository
import com.example.moodmusic.data.local.entity.PlaylistEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class PlaylistsViewModel(
    private val repository: PlaylistRepository
) : ViewModel() {

    val playlists = repository.getPlaylists()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList<PlaylistEntity>()
        )
}