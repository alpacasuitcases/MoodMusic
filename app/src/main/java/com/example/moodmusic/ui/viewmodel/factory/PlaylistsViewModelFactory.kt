package com.example.moodmusic.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moodmusic.data.repository.PlaylistRepository
import com.example.moodmusic.ui.viewmodel.PlaylistsViewModel

class PlaylistsViewModelFactory(
    private val repository: PlaylistRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PlaylistsViewModel(repository) as T
    }
}