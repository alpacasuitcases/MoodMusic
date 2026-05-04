package com.example.moodmusic.domain.model

data class Playlist(
    val id: String,
    val name: String,
    val mood: MoodType,
    val songs: List<Song>
)