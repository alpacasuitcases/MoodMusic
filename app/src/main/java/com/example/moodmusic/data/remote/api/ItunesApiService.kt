package com.example.moodmusic.data.remote.api

import com.example.moodmusic.data.remote.dto.ItunesResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {

    @GET("search")
    suspend fun searchSongs(
        @Query("term") term: String,
        @Query("media") media: String = "music",
        @Query("entity") entity: String = "song",
        @Query("limit") limit: Int = 20
    ): ItunesResponseDto
}