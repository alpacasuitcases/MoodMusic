package com.example.moodmusic.data.local

import com.example.moodmusic.data.local.entity.FavoriteSongEntity
import com.example.moodmusic.data.local.entity.MoodHistoryEntity
import com.example.moodmusic.domain.model.MoodEntry
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.domain.model.Song

fun Song.toEntity(): FavoriteSongEntity {
    return FavoriteSongEntity(
        id = id,
        title = title,
        artist = artist,
        album = album,
        imageUrl = imageUrl,
        previewUrl = previewUrl,
        trackUrl = trackUrl,
        moodTag = moodTag
    )
}

fun FavoriteSongEntity.toDomain(): Song {
    return Song(
        id = id,
        title = title,
        artist = artist,
        album = album,
        imageUrl = imageUrl,
        previewUrl = previewUrl,
        trackUrl = trackUrl,
        moodTag = moodTag
    )
}

fun MoodHistoryEntity.toDomain(): MoodEntry {
    return MoodEntry(
        id = id,
        mood = MoodType.valueOf(mood),
        timestamp = timestamp
    )
}