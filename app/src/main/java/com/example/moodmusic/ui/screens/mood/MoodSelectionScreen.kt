package com.example.moodmusic.ui.screens.mood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moodmusic.domain.model.MoodType

@Composable
fun MoodSelectionScreen(
    onMoodSelected: (MoodType) -> Unit,
    onFavoritesClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Selecciona tu mood")

        MoodType.entries.forEach { mood ->
            Button(onClick = { onMoodSelected(mood) }) {
                Text(mood.name)
            }
        }

        Button(onClick = onFavoritesClick) {
            Text("Favoritos")
        }

        Button(onClick = onHistoryClick) {
            Text("Historial")
        }
    }
}