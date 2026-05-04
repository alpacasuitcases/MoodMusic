package com.example.moodmusic.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moodmusic.data.repository.MusicRepository
import com.example.moodmusic.data.repository.PlaylistRepository
import com.example.moodmusic.ui.viewmodel.SongsViewModel

class SongsViewModelFactory(
    private val repository: MusicRepository,
    private val playlistRepository: PlaylistRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SongsViewModel(
            repository,
            playlistRepository
        ) as T
    }
}