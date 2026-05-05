package com.example.moodmusic.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "playlist_songs",
    primaryKeys = ["playlistId", "songId"]
)
data class PlaylistSongEntity(
    val playlistId: String,
    val songId: String,
    val position: Int,
    val title: String,
    val artist: String,
    val imageUrl: String?,
    val previewUrl: String?,
    val trackUrl: String?
)