package com.example.moodmusic.data.remote.dto

data class ItunesResponseDto(
    val resultCount: Int,
    val results: List<ItunesSongDto>
)