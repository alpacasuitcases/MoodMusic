package com.example.moodmusic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_songs")
data class FavoriteSongEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String,
    val album: String,
    val imageUrl: String?,
    val previewUrl: String?,
    val trackUrl: String?,
    val moodTag: String
)