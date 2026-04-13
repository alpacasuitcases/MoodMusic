package com.example.moodmusic.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moodmusic.data.repository.FavoritesRepository
import com.example.moodmusic.ui.viewmodel.FavoritesViewModel

class FavoritesViewModelFactory(
    private val repository: FavoritesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoritesViewModel(repository) as T
    }
}