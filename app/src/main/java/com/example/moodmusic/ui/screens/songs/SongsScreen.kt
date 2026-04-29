package com.example.moodmusic.ui.screens.songs

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.moodmusic.di.AppContainer
import com.example.moodmusic.player.PreviewPlayerManager
import com.example.moodmusic.ui.components.SongItem
import com.example.moodmusic.ui.viewmodel.FavoritesViewModel
import com.example.moodmusic.ui.viewmodel.SongsViewModel
import com.example.moodmusic.ui.viewmodel.factory.FavoritesViewModelFactory
import com.example.moodmusic.ui.viewmodel.factory.SongsViewModelFactory

@Composable
fun SongsScreen(
    query: String,
    emotionName: String,
    emoji: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    val songsViewModel: SongsViewModel = viewModel(
        factory = SongsViewModelFactory(AppContainer.musicRepository)
    )

    val favoritesViewModel: FavoritesViewModel = viewModel(
        factory = FavoritesViewModelFactory(AppContainer.favoritesRepository)
    )

    val songs by songsViewModel.songs.collectAsState()
    val loading by songsViewModel.loading.collectAsState()
    val error by songsViewModel.error.collectAsState()
    val favoriteIds by favoritesViewModel.favoriteIds.collectAsState()

    val previewManager = remember { PreviewPlayerManager(context) }

    LaunchedEffect(query) {
        songsViewModel.searchSongs(query)
    }

    DisposableEffect(Unit) {
        onDispose {
            previewManager.release()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TextButton(
            onClick = onBackClick,
            modifier = Modifier.padding(start = 8.dp, top = 8.dp)
        ) {
            Text("← Volver")
        }

        Text(
            text = "$emoji Canciones para $emotionName",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Text(
            text = "Búsqueda: $query",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
        )

        when {
            loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Buscando canciones...",
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
            }

            !error.isNullOrBlank() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = error ?: "Error desconocido",
                                style = MaterialTheme.typography.bodyLarge
                            )

                            Button(
                                onClick = { songsViewModel.searchSongs(query) },
                                modifier = Modifier.padding(top = 16.dp)
                            ) {
                                Text("Reintentar")
                            }
                        }
                    }
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(songs, key = { it.id }) { song ->
                        val isFavorite = favoriteIds.contains(song.id)

                        SongItem(
                            song = song,
                            isFavorite = isFavorite,
                            isPreviewPlaying = previewManager.isPlaying(song.previewUrl),
                            onSongClick = {},
                            onToggleFavorite = {
                                favoritesViewModel.toggleFavorite(song, isFavorite)
                            },
                            onPreviewClick = {
                                previewManager.playOrPause(song.previewUrl)
                            },
                            onOpenExternal = {
                                song.trackUrl?.let { url ->
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                    context.startActivity(intent)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}