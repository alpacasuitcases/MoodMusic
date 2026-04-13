package com.example.moodmusic.ui.navigation

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Mood : Routes("mood")
    data object Songs : Routes("songs/{mood}") {
        fun createRoute(mood: String) = "songs/$mood"
    }
    data object Favorites : Routes("favorites")
    data object History : Routes("history")
}