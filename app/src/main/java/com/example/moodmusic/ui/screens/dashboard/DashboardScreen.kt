package com.example.moodmusic.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.ui.components.MoodBackground
import com.example.moodmusic.ui.theme.colorForMood

@Composable
fun DashboardScreen(
    moodLabel: String,
    mood: MoodType,
    onNewMood: () -> Unit,
    onFavorites: () -> Unit,
    onHistory: () -> Unit,
    onPlaylists: () -> Unit,
    onBackToSongs: () -> Unit
) {
    val moodColor = colorForMood(mood)

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(listOf(moodColor, Color(0xFF1A1A2E)))
                )
        )
        MoodBackground(mood = mood, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Cabecera
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "🎶",
                    style = MaterialTheme.typography.displayMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "MoodMusic",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Card(
                    shape = RoundedCornerShape(50.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White.copy(alpha = 0.20f)
                    )
                ) {
                    Text(
                        text = moodLabel,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }

            // Opciones principales
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DashboardButton(
                    emoji = "🎵",
                    label = "Ver canciones actuales",
                    description = "Vuelve a la lista de canciones",
                    onClick = onBackToSongs,
                    containerColor = Color.White.copy(alpha = 0.18f)
                )

                DashboardButton(
                    emoji = "➕",
                    label = "Nueva emoción",
                    description = "Busca música para otra emoción",
                    onClick = onNewMood,
                    containerColor = Color.White.copy(alpha = 0.18f)
                )

                DashboardButton(
                    emoji = "❤️",
                    label = "Ver favoritos",
                    description = "Tus canciones guardadas",
                    onClick = onFavorites,
                    containerColor = Color.White.copy(alpha = 0.18f)
                )

                DashboardButton(
                    emoji = "📋",
                    label = "Ver playlists",
                    description = "Playlists guardadas por emoción",
                    onClick = onPlaylists,
                    containerColor = Color.White.copy(alpha = 0.18f)
                )

                DashboardButton(
                    emoji = "📊",
                    label = "Ver historial emocional",
                    description = "Evolución y gráficas de tus emociones",
                    onClick = onHistory,
                    containerColor = Color.White.copy(alpha = 0.18f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun DashboardButton(
    emoji: String,
    label: String,
    description: String,
    onClick: () -> Unit,
    containerColor: Color
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(emoji, style = MaterialTheme.typography.headlineSmall)
            Column {
                Text(
                    text = label,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.70f)
                )
            }
        }
    }
}
