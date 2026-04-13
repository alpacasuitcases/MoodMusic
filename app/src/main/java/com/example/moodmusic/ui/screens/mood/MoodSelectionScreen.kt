package com.example.moodmusic.ui.screens.mood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "MoodMusic",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "¿Cómo te sientes hoy?",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp)
        )

        MoodType.entries.forEach { mood ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Button(
                    onClick = { onMoodSelected(mood) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text(
                        text = mood.label,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onFavoritesClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver favoritos")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = onHistoryClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver historial")
        }
    }
}