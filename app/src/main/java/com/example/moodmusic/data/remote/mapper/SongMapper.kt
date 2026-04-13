package com.example.moodmusic.data.remote.mapper

import com.example.moodmusic.data.remote.dto.ItunesSongDto
import com.example.moodmusic.domain.model.Song

fun ItunesSongDto.toDomain(moodTag: String): Song {
    return Song(
        id = trackId?.toString() ?: "",
        title = trackName.orEmpty(),
        artist = artistName.orEmpty(),
        album = collectionName.orEmpty(),
        imageUrl = artworkUrl100,
        previewUrl = previewUrl,
        trackUrl = trackViewUrl,
        moodTag = moodTag
    )
}