package com.example.moodmusic.domain.model

enum class MoodType(val query: String, val label: String) {
    HAPPY("happy pop", "Feliz"),
    SAD("sad acoustic", "Triste"),
    FOCUS("focus instrumental", "Concentrado"),
    RELAX("relax chill", "Relajado"),
    ENERGY("workout dance", "Con energía")
}