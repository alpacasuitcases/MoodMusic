package com.example.moodmusic.data.remote.dto

data class ItunesSongDto(
    val trackId: Long?,
    val trackName: String?,
    val artistName: String?,
    val collectionName: String?,
    val artworkUrl100: String?,
    val previewUrl: String?,
    val trackViewUrl: String?
)