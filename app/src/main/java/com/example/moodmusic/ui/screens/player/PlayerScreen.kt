package com.example.moodmusic.ui.screens.player

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.domain.model.Song
import com.example.moodmusic.player.PreviewPlayerManager
import com.example.moodmusic.ui.components.BackButton
import com.example.moodmusic.ui.components.MoodBackground
import com.example.moodmusic.ui.theme.colorForMood
import kotlinx.coroutines.delay

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
        currentSong?.previewUrl?.takeIf { it.isNotBlank() }?.let { url ->
            previewManager.stop()
            previewManager.playOrPause(url)
        }
    }

    // Avanzar barra de progreso
    LaunchedEffect(isPlaying, currentIndex) {
        while (isPlaying) {
            delay(300L)
            progress = (progress + 0.01f).coerceAtMost(1f)
            if (progress >= 1f) {
                if (isLoop) {
                    progress = 0f
                    currentSong?.previewUrl?.let { previewManager.stop(); previewManager.playOrPause(it) }
                } else if (currentIndex < songs.size - 1) {
                    currentIndex++
                }
            }
        }
    }

    DisposableEffect(Unit) { onDispose { previewManager.release() } }

    // UI
    Box(modifier = Modifier.fillMaxSize()) {

        // Fondo dinámico
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.verticalGradient(listOf(colorForMood(mood), Color.Black)))
        )
        MoodBackground(mood = mood, modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Fila superior: back + título de sección
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(
                    onClick = { previewManager.stop(); navController.popBackStack() },
                    tint = Color.White,
                    containerColor = Color.White.copy(alpha = 0.20f)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Reproduciendo",
                    style = MaterialTheme.typography.labelLarge,
                    color = Color.White.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.weight(1f))
                // Espacio simétrico al BackButton
                Spacer(modifier = Modifier.size(52.dp))
            }

            // Carátula
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .shadow(elevation = 24.dp, shape = RoundedCornerShape(28.dp))
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color.White.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                if (!currentSong?.imageUrl.isNullOrBlank()) {
                    AsyncImage(
                        model = currentSong?.imageUrl,
                        contentDescription = currentSong?.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("🎵", style = MaterialTheme.typography.displayLarge)
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Título y artista
            Text(
                text = currentSong?.title ?: "—",
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = currentSong?.artist ?: "",
                style = MaterialTheme.typography.titleSmall,
                color = Color.White.copy(alpha = 0.75f),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Barra de progreso
            Slider(
                value = progress,
                onValueChange = { progress = it },
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTrackColor = Color.White.copy(alpha = 0.25f)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            // Tiempo (preview siempre ~30s)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatSeconds((progress * 30).toInt()),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.55f)
                )
                Text(
                    text = "0:30",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.55f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Controles principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Anterior
                IconButton(
                    onClick = { if (currentIndex > 0) { currentIndex--; progress = 0f } },
                    enabled = currentIndex > 0,
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            Color.White.copy(alpha = if (currentIndex > 0) 0.18f else 0.07f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.SkipPrevious,
                        contentDescription = "Anterior",
                        tint = Color.White.copy(alpha = if (currentIndex > 0) 1f else 0.35f),
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Play / Pausa — botón principal grande
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .shadow(elevation = 12.dp, shape = CircleShape)
                        .background(Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(
                        onClick = {
                            currentSong?.previewUrl?.let { previewManager.playOrPause(it) }
                        },
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Pausar" else "Reproducir",
                            tint = colorForMood(mood),
                            modifier = Modifier.size(42.dp)
                        )
                    }
                }

                // Siguiente
                IconButton(
                    onClick = { if (currentIndex < songs.size - 1) { currentIndex++; progress = 0f } },
                    enabled = currentIndex < songs.size - 1,
                    modifier = Modifier
                        .size(56.dp)
                        .background(
                            Color.White.copy(alpha = if (currentIndex < songs.size - 1) 0.18f else 0.07f),
                            CircleShape
                        )
                ) {
                    Icon(
                        Icons.Default.SkipNext,
                        contentDescription = "Siguiente",
                        tint = Color.White.copy(alpha = if (currentIndex < songs.size - 1) 1f else 0.35f),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Fila secundaria: Loop + iTunes
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Loop
                OutlinedButton(
                    onClick = { isLoop = !isLoop },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (isLoop) Color.White.copy(alpha = 0.18f) else Color.Transparent,
                        contentColor = Color.White
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isLoop) Color.White else Color.White.copy(alpha = 0.4f)
                    )
                ) {
                    Icon(
                        Icons.Default.Repeat,
                        contentDescription = "Loop",
                        modifier = Modifier.size(16.dp),
                        tint = if (isLoop) Color.White else Color.White.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        if (isLoop) "Loop ON" else "Loop",
                        color = if (isLoop) Color.White else Color.White.copy(alpha = 0.6f)
                    )
                }

                // iTunes
                OutlinedButton(
                    onClick = {
                        currentSong?.trackUrl?.let {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                        }
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp, Color.White.copy(alpha = 0.4f)
                    )
                ) {
                    Text("🎵 iTunes", color = Color.White.copy(alpha = 0.8f))
                }
            }
        }
    }
}

private fun formatSeconds(s: Int): String {
    val m = s / 60
    val sec = s % 60
    return "$m:${sec.toString().padStart(2, '0')}"
}