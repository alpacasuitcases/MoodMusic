package com.example.moodmusic.domain.model

data class MoodEntry(
    val id: Int = 0,
    val mood: MoodType,
    val timestamp: Long
)