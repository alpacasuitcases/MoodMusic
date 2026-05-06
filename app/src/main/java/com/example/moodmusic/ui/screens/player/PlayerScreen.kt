package com.example.moodmusic.ui.screens.player

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.domain.model.Song
import com.example.moodmusic.player.PreviewPlayerManager
import com.example.moodmusic.ui.components.MoodBackground
import com.example.moodmusic.ui.theme.colorForMood
import kotlinx.coroutines.delay
import com.example.moodmusic.ui.components.BackButton

@Composable
fun PlayerScreen(
    songs: List<Song>,
    startIndex: Int,
    mood: MoodType,
    navController: NavController
) {

    val context = LocalContext.current

    val previewManager = remember { PreviewPlayerManager(context) }
    val playingUrl by previewManager.playingUrl.collectAsState()

    var currentIndex by remember { mutableStateOf(startIndex) }
    val currentSong = songs.getOrNull(currentIndex)

    var isLoop by remember { mutableStateOf(false) }

    var progress by remember { mutableStateOf(0f) }
    val isPlaying = playingUrl != null && playingUrl == currentSong?.previewUrl

    // Autoplay al cambiar de canción
    LaunchedEffect(currentIndex) {
        progress = 0f
        val url = currentSong?.previewUrl
        if (!url.isNullOrBlank()) {
            previewManager.stop()
            previewManager.playOrPause(url)
        }
    }
    // Avanzar la barra de progreso mientras suena
    LaunchedEffect(isPlaying, currentIndex) {
        while (isPlaying) {
            delay(300L)
            progress = (progress + 0.01f).coerceAtMost(1f)
            if (progress >= 1f) {
                if (isLoop) {
                    progress = 0f
                    currentSong?.previewUrl?.let {
                        previewManager.stop()
                        previewManager.playOrPause(it)
                    }
                } else if (currentIndex < songs.size - 1) {
                    currentIndex++
                }
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose { previewManager.release() }
    }
    // Interfaz de usuario
    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo dinámico
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(colorForMood(mood), Color.Black)
                    )
                )
        )
        MoodBackground(mood = mood, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            BackButton(
                onClick = { previewManager.stop(); navController.popBackStack() },
                tint = Color.White
            )

            // Carátula
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                if (!currentSong?.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = currentSong?.imageUrl,
                        contentDescription = currentSong?.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(28.dp))
                    )
                } else {
                    Text("🎵", style = MaterialTheme.typography.displayLarge)
                }
            }

            // Título, artista, album
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = currentSong?.title ?: "—",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = currentSong?.artist ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.80f),
                    textAlign = TextAlign.Center
                )
                if (!currentSong?.album.isNullOrBlank()) {
                    Text(
                        text = currentSong?.album ?: "",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.60f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Barra de progreso
            Slider(
                value = progress,
                onValueChange = { progress = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.28f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Controles
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            ) {
                // Anterior
                FilledTonalButton(
                    onClick = { if (currentIndex > 0) { currentIndex--; progress = 0f } },
                    enabled = currentIndex > 0,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color.White.copy(alpha = 0.25f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(56.dp)
                ) { Text("⏮", style = MaterialTheme.typography.titleLarge) }

                // Play / Pausa principal
                Button(
                    onClick = { currentSong?.previewUrl?.let { url -> previewManager.playOrPause(url) } },
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        text = if (isPlaying) "⏸" else "▶",
                        style = MaterialTheme.typography.headlineMedium,
                        color = Color(0xFF5C6BC0)
                    )
                }

                // Siguiente
                FilledTonalButton(
                    onClick = { if (currentIndex < songs.size - 1) { currentIndex++; progress = 0f } },
                    enabled = currentIndex < songs.size - 1,
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Color.White.copy(alpha = 0.25f),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.size(56.dp)
                ) { Text("⏭", style = MaterialTheme.typography.titleLarge) }
            }


            // Bucle y abrir en completo
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = { isLoop = !isLoop },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                ) {
                    Text(if (isLoop) "🔁 Loop ON" else "🔁 Loop OFF")
                }

                OutlinedButton(
                    onClick = {
                        currentSong?.trackUrl?.let {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.5f))
                ) {
                    Text("🎵 iTunes")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}