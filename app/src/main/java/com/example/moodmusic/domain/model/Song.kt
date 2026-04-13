package com.example.moodmusic.domain.model

data class Song(
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val imageUrl: String?,
    val previewUrl: String?,
    val trackUrl: String?,
    val moodTag: String
)