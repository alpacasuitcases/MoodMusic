package com.example.moodmusic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mood_history")
data class MoodHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val moodName: String,
    val timestamp: Long
)