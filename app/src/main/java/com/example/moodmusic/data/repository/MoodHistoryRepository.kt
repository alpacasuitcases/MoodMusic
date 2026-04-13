package com.example.moodmusic.data.repository

import com.example.moodmusic.data.local.dao.MoodHistoryDao
import com.example.moodmusic.data.local.entity.MoodHistoryEntity
import com.example.moodmusic.data.local.toDomain
import com.example.moodmusic.domain.model.MoodEntry
import com.example.moodmusic.domain.model.MoodType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoodHistoryRepository(
    private val dao: MoodHistoryDao
) {
    fun getMoodHistory(): Flow<List<MoodEntry>> {
        return dao.getAllMoodHistory().map { list -> list.map { it.toDomain() } }
    }

    suspend fun insertMood(mood: MoodType) {
        dao.insert(
            MoodHistoryEntity(
                mood = mood.name,
                timestamp = System.currentTimeMillis()
            )
        )
    }
}