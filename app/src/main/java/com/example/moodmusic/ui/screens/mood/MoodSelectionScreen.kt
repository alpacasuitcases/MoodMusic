package com.example.moodmusic.ui.screens.mood

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.domain.model.mapToMood
import com.example.moodmusic.ui.components.MoodPicker

@Composable
fun MoodSelectionScreen(
    onMoodSelected: (MoodType) -> Unit,
    onFavoritesClick: () -> Unit,
    onHistoryClick: () -> Unit
) {

    var selectedMood by remember { mutableStateOf<MoodType?>(null) }

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

        // 🔥 MOOD PICKER (NUEVO)
        MoodPicker { x, y ->
            selectedMood = mapToMood(x, y)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 MOSTRAR MOOD
        Text(
            text = "Estado: ${selectedMood?.emoji ?: ""} ${selectedMood?.label ?: ""}",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 BOTÓN PARA IR A CANCIONES
        Button(
            onClick = {
                selectedMood?.let { onMoodSelected(it) }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedMood != null
        ) {
            Text("Buscar canciones")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // 🔽 LO QUE YA TENÍAS
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