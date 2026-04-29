package com.example.moodmusic.ui.screens.mood

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moodmusic.domain.model.EmotionLabel
import com.example.moodmusic.ui.components.MoodPicker

@Composable
fun MoodSelectionScreen(
    onEmotionSelected: (EmotionLabel) -> Unit,
    onFavoritesClick: () -> Unit,
    onHistoryClick: () -> Unit
) {
    var selectedEmotion by remember { mutableStateOf<EmotionLabel?>(null) }

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
            modifier = Modifier.padding(top = 8.dp, bottom = 14.dp)
        )

        MoodPicker { emotion ->
            selectedEmotion = emotion
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Text(
                text = if (selectedEmotion != null)
                    "Estado seleccionado: ${selectedEmotion!!.emoji} ${selectedEmotion!!.text}"
                else
                    "Mueve el punto sobre la rueda para elegir una emoción",
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
            enabled = selectedEmotion != null

        ) {
            Text("Buscar canciones")
        }

        Spacer(modifier = Modifier.height(22.dp))

        OutlinedButton(
            onClick = onFavoritesClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver favoritos")
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = onHistoryClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver historial")
        }
    }
}