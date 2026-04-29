package com.example.moodmusic.domain.model

data class EmotionLabel(
    val text: String,
    val emoji: String,
    val angle: Float,
    val distance: Float,
    val mood: MoodType,
    val query: String
)