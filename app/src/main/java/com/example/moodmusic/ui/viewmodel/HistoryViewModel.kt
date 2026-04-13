package com.example.moodmusic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmusic.data.repository.MoodHistoryRepository
import com.example.moodmusic.domain.model.MoodEntry
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoryViewModel(
    private val repository: MoodHistoryRepository
) : ViewModel() {

    val history: StateFlow<List<MoodEntry>> = repository.getAllHistory()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addMood(moodName: String) {
        viewModelScope.launch {
            repository.addMood(moodName)
        }
    }
}