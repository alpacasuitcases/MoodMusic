package com.example.moodmusic.ui.navigation

import android.net.Uri

sealed class Routes(val route: String) {
    data object Home : Routes("home")
    data object Mood : Routes("mood")

    data object Songs : Routes("songs/{query}/{emotionName}/{emoji}/{mood}") {

        fun createRoute(query: String, emotionName: String, emoji: String, mood: String): String {
            return "songs/${Uri.encode(query)}/${Uri.encode(emotionName)}/${Uri.encode(emoji)}/$mood"
        }
    }

    data object SongsRoot : Routes("songs")

    data object Favorites : Routes("favorites")
    data object History : Routes("history")

    data object Playlists : Routes("playlists")

    data object PlaylistDetail : Routes("playlistDetail/{playlistId}") {

        fun createRoute(id: String) =
            "playlistDetail/$id"
    }

    data object Player : Routes("player/{index}/{mood}") {
        fun createRoute(index: Int, mood: String) =
            "player/$index/$mood"
    }

    data object Dashboard : Routes("dashboard/{mood}/{emotion}") {
        fun createRoute(mood: String, emotion: String) =
            "dashboard/$mood/$emotion"
    }
}