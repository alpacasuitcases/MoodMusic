package com.example.moodmusic.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.moodmusic.domain.model.MoodType

fun colorForMood(mood: MoodType): Color {
    return when (mood) {
        MoodType.HAPPY -> Color(0xFFFFD166)
        MoodType.RELAX -> Color(0xFF7BDCB5)
        MoodType.SAD -> Color(0xFF74C0FC)
        MoodType.ENERGY -> Color(0xFFFF9F6E)
        MoodType.FOCUS -> Color(0xFFB197FC)
        MoodType.ANGRY -> Color(0xFFFF6B6B)
    }
}