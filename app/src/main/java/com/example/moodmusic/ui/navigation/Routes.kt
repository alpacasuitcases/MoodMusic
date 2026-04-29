package com.example.moodmusic.ui.navigation

import android.net.Uri

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Mood : Routes("mood")

    data object Songs : Routes("songs/{query}/{emotionName}/{emoji}") {
        fun createRoute(query: String, emotionName: String, emoji: String): String {
            return "songs/${Uri.encode(query)}/${Uri.encode(emotionName)}/${Uri.encode(emoji)}"
        }
    }

    data object Favorites : Routes("favorites")
    data object History : Routes("history")
}