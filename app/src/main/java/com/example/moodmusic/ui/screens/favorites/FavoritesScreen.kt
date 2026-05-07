package com.example.moodmusic.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.player.PreviewPlayerManager
import com.example.moodmusic.ui.components.BackButton
import com.example.moodmusic.ui.components.SongItem
import com.example.moodmusic.ui.viewmodel.FavoritesViewModel
import com.example.moodmusic.ui.viewmodel.factory.FavoritesViewModelFactory

@Composable
fun FavoritesScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val previewManager = remember { PreviewPlayerManager(context) }

    // ← CLAVE: observar playingUrl como estado reactivo
    val playingUrl by previewManager.playingUrl.collectAsState()

    val viewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(AppContainer.favoritesRepository)
    )

    val favorites by viewModel.favorites.collectAsState()
    val favoriteIds by viewModel.favoriteIds.collectAsState()

    DisposableEffect(Unit) { onDispose { previewManager.release() } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    // Colores suaves en la misma línea que el Home (lila claro → blanco)
                    listOf(
                        Color(0xFFE8EAF6),
                        Color(0xFFF3F0FF),
                        Color(0xFFF7F7FB)
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // Header con espacio para que el título no quede pegado arriba
            Spacer(modifier = Modifier.height(48.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 4.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton(onClick = onBackClick)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Favoritos ❤️",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (favorites.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(32.dp)
                    ) {
                        Text("🤍", style = MaterialTheme.typography.displayMedium)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Todavía no tienes favoritos",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Añade canciones desde cualquier lista",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                            modifier = Modifier.padding(top = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    items(favorites, key = { it.id }) { song ->
                        SongItem(
                            song = song,
                            isFavorite = favoriteIds.contains(song.id),
                            // ← usar playingUrl del estado reactivo
                            isPreviewPlaying = playingUrl == song.previewUrl,
                            onSongClick = {},
                            onToggleFavorite = { viewModel.removeFavorite(song.id) },
                            onPreviewClick = { previewManager.playOrPause(song.previewUrl) }
                        )
                    }
                }
            }
        }
    }
}