package com.example.moodmusic.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moodmusic.data.local.entity.FavoriteSongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteSongDao {

    @Query("SELECT * FROM favorite_songs ORDER BY title ASC")
    fun getAllFavorites(): Flow<List<FavoriteSongEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(song: FavoriteSongEntity)

    @Query("DELETE FROM favorite_songs WHERE id = :songId")
    suspend fun deleteFavorite(songId: String)
}