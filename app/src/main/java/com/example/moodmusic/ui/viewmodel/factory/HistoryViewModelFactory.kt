package com.example.moodmusic.ui.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moodmusic.data.repository.MoodHistoryRepository
import com.example.moodmusic.ui.viewmodel.HistoryViewModel

class HistoryViewModelFactory(
    private val repository: MoodHistoryRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryViewModel(repository) as T
    }
}