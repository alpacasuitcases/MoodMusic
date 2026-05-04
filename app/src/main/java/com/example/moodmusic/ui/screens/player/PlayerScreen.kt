package com.example.moodmusic.ui.screens.player

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.moodmusic.domain.model.MoodType
import com.example.moodmusic.domain.model.Song
import com.example.moodmusic.player.PreviewPlayerManager
import com.example.moodmusic.ui.components.MoodBackground
import com.example.moodmusic.ui.theme.colorForMood

@Composable
fun PlayerScreen(
    songs: List<Song>,
    startIndex: Int,
    mood: MoodType,
    navController: NavController
) {

    val context = LocalContext.current
    val previewManager = remember { PreviewPlayerManager(context) }

    var currentIndex by remember { mutableStateOf(startIndex) }
    val currentSong = songs.getOrNull(currentIndex)

    var isPlaying by remember { mutableStateOf(false) }
    var isLoop by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        onDispose { previewManager.release() }
    }

    // AUTOPLAY
    LaunchedEffect(currentIndex) {
        currentSong?.previewUrl?.let {
            previewManager.playOrPause(it)
            isPlaying = true
        }
    }

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

        MoodBackground(mood)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            TextButton(onClick = { navController.popBackStack() }) {
                Text("← Volver")
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                AsyncImage(
                    model = currentSong?.imageUrl,
                    contentDescription = "",
                    modifier = Modifier.size(260.dp),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentSong?.title ?: "",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = currentSong?.artist ?: "",
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                // ▶️ PLAY / PAUSE
                Button(
                    onClick = {
                        previewManager.playOrPause(currentSong?.previewUrl)
                        isPlaying = !isPlaying
                    }
                ) {
                    Text(if (isPlaying) "Pause ⏸" else "Play ▶")
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {

                    // ⏮ ANTERIOR
                    Button(onClick = {
                        if (currentIndex > 0) {
                            currentIndex--
                        }
                    }) {
                        Text("⏮")
                    }

                    // ⏭ SIGUIENTE
                    Button(onClick = {
                        if (currentIndex < songs.size - 1) {
                            currentIndex++
                        } else if (isLoop) {
                            currentIndex = 0
                        }
                    }) {
                        Text("⏭")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 🔁 LOOP
                OutlinedButton(
                    onClick = { isLoop = !isLoop }
                ) {
                    Text(if (isLoop) "Loop ON 🔁" else "Loop OFF")
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 🌍 ABRIR COMPLETA
                OutlinedButton(
                    onClick = {
                        currentSong?.trackUrl?.let {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                            context.startActivity(intent)
                        }
                    }
                ) {
                    Text("Abrir completa")
                }
            }
        }
    }
}