package com.example.moodmusic.ui.screens.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moodmusic.domain.model.EmotionLabel
import com.example.moodmusic.ui.components.MoodPicker
import com.example.moodmusic.ui.theme.AppBackground
import com.example.moodmusic.ui.theme.colorForMood

@Composable
fun MoodSelectionScreen(
    onEmotionSelected: (EmotionLabel) -> Unit
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
            text = "¿Cómo te sientes hoy?",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 6.dp, bottom = 12.dp)
        )

        // Rueda de emociones (Flower Picker)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(28.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                MoodPicker { emotion ->
                    selectedEmotion = emotion
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Indicador de emoción seleccionada
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

        // Botón principal
        Button(
            onClick = { selectedEmotion?.let { onEmotionSelected(it) } },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedEmotion != null,
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = selectedEmotion?.let { colorForMood(it.mood) }
                    ?: MaterialTheme.colorScheme.primary
            )
        ) {
            Text("🎵 Buscar canciones")
        }
    }
}
