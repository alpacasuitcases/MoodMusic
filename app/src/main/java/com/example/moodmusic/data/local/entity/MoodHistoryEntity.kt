package com.example.moodmusic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_history")
data class MoodHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val mood: String,
    val timestamp: Long
)