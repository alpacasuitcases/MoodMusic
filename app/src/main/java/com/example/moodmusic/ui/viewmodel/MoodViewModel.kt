package com.example.moodmusic.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moodmusic.data.repository.MoodHistoryRepository
import com.example.moodmusic.domain.model.MoodType
import kotlinx.coroutines.launch

class MoodViewModel(
    private val repository: MoodHistoryRepository
) : ViewModel() {

    fun saveMood(mood: MoodType) {
        viewModelScope.launch {
            repository.insertMood(mood)
        }
    }
}