package com.example.moodmusic.ui.screens.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moodmusic.domain.model.EmotionLabel
import com.example.moodmusic.ui.components.MoodPicker
import com.example.moodmusic.ui.theme.AppBackground
import com.example.moodmusic.ui.theme.colorForMood

@Composable
fun MoodSelectionScreen(
    onEmotionSelected: (EmotionLabel) -> Unit,
    onFavoritesClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onPlaylistsClick: () -> Unit
) {
    var selectedEmotion by remember { mutableStateOf<EmotionLabel?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
            .padding(22.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "MoodMusic",
            style = MaterialTheme.typography.headlineLarge
        )

        Text(
            text = "Elige una emoción y te prepararemos una lista",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                MoodPicker { emotion ->
                    selectedEmotion = emotion
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = if (selectedEmotion != null)
                    "Estado seleccionado: ${selectedEmotion!!.emoji} ${selectedEmotion!!.text}"
                else
                    "Mueve el punto sobre la flor para elegir una emoción",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
            onClick = {
                selectedEmotion?.let { onEmotionSelected(it) }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedEmotion != null,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = selectedEmotion?.let { colorForMood(it.mood) }
                    ?: MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Buscar canciones")
        }

        Spacer(modifier = Modifier.height(22.dp))

        OutlinedButton(
            onClick = onFavoritesClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("Ver favoritos")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onHistoryClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(50.dp)
        ) {
            Text("Ver historial")
        }

        OutlinedButton(
            onClick = onPlaylistsClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver playlists")
        }
    }
}