package com.example.moodmusic.domain.model

enum class MoodType(val query: String, val label: String, val emoji: String) {
    HAPPY("happy pop", "Feliz", "😄"),
    SAD("sad acoustic", "Triste", "\uD83D\uDE22"),
    FOCUS("focus instrumental", "Concentrado", "\uD83E\uDD13\u200B"),
    RELAX("relax chill", "Relajado", "\uD83E\uDDD8"),
    ENERGY("workout dance", "Con energía", "\uD83D\uDE01\u200B"),
    ANGRY("angry rock", "Enfadado", "\uD83D\uDE21")
}