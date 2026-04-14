package com.example.moodmusic.domain.model

fun mapToMood(x: Float, y: Float): MoodType {
    return when {
        x > 0.5f && y < 0.5f -> MoodType.HAPPY
        x < 0.5f && y < 0.5f -> MoodType.ENERGY
        x < 0.5f && y > 0.5f -> MoodType.SAD
        else -> MoodType.RELAX
    }
}