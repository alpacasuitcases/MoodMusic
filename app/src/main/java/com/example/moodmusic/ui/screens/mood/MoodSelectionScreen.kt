package com.example.moodmusic.ui.screens.mood

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moodmusic.domain.model.EmotionLabel
import com.example.moodmusic.ui.components.MoodPicker
import com.example.moodmusic.ui.theme.AppBackground
import com.example.moodmusic.ui.theme.AppPrimary
import com.example.moodmusic.ui.theme.colorForMood

@Composable
fun MoodSelectionScreen(
    onEmotionSelected: (EmotionLabel) -> Unit,
    onFavorites: () -> Unit,
    onPlaylists: () -> Unit,
    onHistory: () -> Unit
) {
    var selectedEmotion by remember { mutableStateOf<EmotionLabel?>(null) }

    Scaffold(
        containerColor = AppBackground,
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onFavorites,
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favoritos") },
                    label = { Text("Favoritos") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onPlaylists,
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "Playlists") },
                    label = { Text("Playlists") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onHistory,
                    icon = { Icon(Icons.Default.History, contentDescription = "Historial") },
                    label = { Text("Historial") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 22.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "MoodMusic",
                style = MaterialTheme.typography.headlineLarge,
                color = AppPrimary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "¿Cómo te sientes hoy?",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 6.dp, bottom = 20.dp),
                textAlign = TextAlign.Center
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    MoodPicker { emotion -> selectedEmotion = emotion }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
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

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { selectedEmotion?.let { onEmotionSelected(it) } },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                enabled = selectedEmotion != null,
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = selectedEmotion?.let { colorForMood(it.mood) }
                        ?: MaterialTheme.colorScheme.primary
                )
            ) {
                Text("🎵 Buscar canciones", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}